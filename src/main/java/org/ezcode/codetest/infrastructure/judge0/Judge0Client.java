package org.ezcode.codetest.infrastructure.judge0;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.response.compile.ExecutionResultResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import io.netty.channel.ChannelOption;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class Judge0Client implements JudgeClient {

    @Value("${external.judge0.url}")
    private String judge0ApiUrl;
    private WebClient webClient;
    private final Judge0ResponseMapper interpreter;

    @PostConstruct
    private void init() {
        ConnectionProvider provider = ConnectionProvider.builder("judge0-pool")
            .maxConnections(500)
            .pendingAcquireMaxCount(1000)
            .pendingAcquireTimeout(Duration.ofSeconds(60))
            .build();

        HttpClient httpClient = HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
            .responseTimeout(Duration.ofSeconds(30));

        this.webClient = WebClient.builder()
            .baseUrl(judge0ApiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Override
    public String submitAndGetToken(CodeCompileRequest request) {

        ExecutionResultResponse resp = webClient.post()
            .uri("/submissions?base64_encoded=false&wait=false")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.GATEWAY_TIMEOUT,
                res -> Mono.error(new TimeoutException("Upstream 504 Gateway Timeout"))
            )
            .bodyToMono(ExecutionResultResponse.class)
            .timeout(Duration.ofSeconds(30))
            .retryWhen(Retry.backoff(5, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(4))
                .filter(ex -> {
                    if (ex instanceof TimeoutException) {
                        return true;
                    }
                    if (ex instanceof WebClientResponseException) {
                        HttpStatusCode status = ((WebClientResponseException) ex).getStatusCode();
                        return status.is5xxServerError();
                    }
                    return false;
                })
            )
            .onErrorMap(IllegalStateException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT))
            .onErrorMap(TimeoutException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT))
            .onErrorMap(WebClientResponseException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR))
            .block();

        if (resp == null || resp.token() == null) {
            throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
        }

        return resp.token();
    }

    @Override
    public JudgeResult pollUntilDone(String token) {
        ExecutionResultResponse finalResp = Flux
            .interval(Duration.ZERO, Duration.ofSeconds(1))
            .flatMap(tick ->
                webClient.get()
                    .uri("/submissions/{token}", token)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ExecutionResultResponse.class)
                    .onErrorResume(WebClientResponseException.BadRequest.class,
                        ex -> Mono.just(ExecutionResultResponse.ofCompileError())
                    )
            )
            .filter(resp -> resp.status().id() >= 3)
            .next()
            .timeout(Duration.ofSeconds(60),
                Mono.error(new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT)))
            .block();

        if (finalResp == null) {
            throw new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR);
        }

        return interpreter.toJudgeResult(finalResp);
    }
}
