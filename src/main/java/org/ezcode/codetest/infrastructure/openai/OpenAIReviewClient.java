package org.ezcode.codetest.infrastructure.openai;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.model.OpenAIResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.submission.exception.CodeReviewException;
import org.ezcode.codetest.domain.submission.exception.code.CodeReviewExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIReviewClient implements ReviewClient {

    @Value("${openai.api.url}")
    private String openApiUrl;

    @Value("${openai.api.key}")
    private String openApiKey;
    private WebClient webClient;
    private final OpenAIMessageBuilder openAiMessageBuilder;
    private final OpenAIResponseValidator openAiResponseValidator;
    private final ExceptionNotifier exceptionNotifier;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
            .baseUrl(openApiUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openApiKey)
            .build();
    }

    @Override
    public ReviewResult requestReview(ReviewPayload reviewPayload) {
        Map<String, Object> requestBody = openAiMessageBuilder.buildRequestBody(reviewPayload);

        String content;
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                content = callChatApi(requestBody);
            } catch (CodeReviewException e) {
                log.error("OpenAI API 호출 실패: {}, {}", e.getHttpStatus(), e.getMessage());
                exceptionNotifier.notifyException("requestReview", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ReviewResult(openAiMessageBuilder.buildErrorMessage());
            }

            if (openAiResponseValidator.isValidFormat(content, reviewPayload.isCorrect())) {
                return new ReviewResult(content);
            }
            log.warn("[{}/{}][isCorrect={}] 포맷 검증 실패:\n{}", attempt, maxAttempts, reviewPayload.isCorrect(), content);
        }
        exceptionNotifier.notifyException("requestReview", new CodeReviewException(CodeReviewExceptionCode.REVIEW_INVALID_FORMAT));
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return new ReviewResult(openAiMessageBuilder.buildErrorMessage());
    }

    private String callChatApi(Map<String, Object> requestBody) {
        OpenAIResponse response = webClient.post()
            .uri("/v1/chat/completions")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(OpenAIResponse.class)
            .timeout(Duration.ofSeconds(20))
            .retryWhen(
                Retry.backoff(3, Duration.ofSeconds(1))
                    .maxBackoff(Duration.ofSeconds(5))
                    .filter(ex -> {
                        if (ex instanceof TimeoutException) {
                            return true;
                        }
                        if (ex instanceof WebClientResponseException) {
                            HttpStatusCode statusCode = ((WebClientResponseException) ex).getStatusCode();
                            // 5xx 서버 오류만 재시도 (502, 503 등)
                            return statusCode.is5xxServerError();
                        }
                        return false;
                    })
                    .onRetryExhaustedThrow((spec, signal) -> signal.failure())
            )
            .onErrorMap(WebClientResponseException.class, ex -> {
                HttpStatusCode statusCode = ex.getStatusCode();
                String responseBody = ex.getResponseBodyAsString();
                log.error("OpenAI API 호출 실패 - Status: {}, Response Body: {}", statusCode, responseBody, ex);
                return new CodeReviewException(CodeReviewExceptionCode.REVIEW_SERVER_ERROR);
            })
            .onErrorMap(TimeoutException.class, ex -> {
                log.error("OpenAI API 호출 타임아웃", ex);
                return new CodeReviewException(CodeReviewExceptionCode.REVIEW_TIMEOUT);
            })
            .block();

        return Objects.requireNonNull(response).getReviewContent();
    }
}
