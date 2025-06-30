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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
        this.webClient = WebClient.builder()
            .baseUrl(judge0ApiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public String submitAndGetToken(CodeCompileRequest request) {

        ExecutionResultResponse resp = webClient.post()
            .uri("/submissions?base64_encoded=false&wait=false")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ExecutionResultResponse.class)
            .timeout(Duration.ofSeconds(5))
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(4))
                .filter(ex -> ex instanceof WebClientResponseException
                                     || ex instanceof TimeoutException)
            )
            .onErrorMap(IllegalStateException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT))
            .onErrorMap(WebClientResponseException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR))
            .onErrorMap(TimeoutException.class,
                ex -> new SubmissionException(SubmissionExceptionCode.COMPILE_TIMEOUT))

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
