package org.ezcode.codetest.infrastructure.openai;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.model.OpenAIResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.submission.exception.CodeReviewException;
import org.ezcode.codetest.domain.submission.exception.code.CodeReviewExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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

	@Value("${OPEN_API_URL}")
	private String openApiUrl;

	@Value("${OPEN_API_KEY}")
	private String openApiKey;
	private WebClient webClient;
	private final OpenAIMessageBuilder openAiMessageBuilder;
	private final OpenAIResponseValidator openAiResponseValidator;


	@PostConstruct
	private void init() {
		this.webClient = WebClient.create(openApiUrl);
	}

	@Override
	public ReviewResult requestReview(ReviewPayload request) {
		String userPrompt = openAiMessageBuilder.buildPrompt(request);
		String content;
		int maxAttempts = 3;

		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				content = callChatApi(userPrompt);
			} catch (CodeReviewException e) {
				log.error("OpenAI API 호출 실패: {}, {}", e.getHttpStatus(), e.getMessage());
				return new ReviewResult(openAiMessageBuilder.buildErrorMessage());
			}

			if (openAiResponseValidator.isValidFormat(content, request.isCorrect())) {
				return new ReviewResult(content);
			}
			log.warn("[{}/{}] 포맷 검증 실패:\n{}", attempt, maxAttempts, content);
		}

		return new ReviewResult(openAiMessageBuilder.buildErrorMessage());
	}

	private String callChatApi (String userPrompt){
		Map<String, Object> requestBody = Map.of(
			"model", "o4-mini",
			"messages", List.of(
				Map.of("role", "system", "content", """
													당신은 코딩 테스트 사이트의 코드 리뷰어입니다.
													아래 **정확히** 이 형식을 지켜 응답하세요:
													
													<정답일 경우>
													- 시간 복잡도: Big-O 표기법으로만 답하세요. **단, N과 M을 같다고 가정하고 n으로 표기하세요.**
													- 코드에 포함된 중첩 루프(depth)에 따라 O(N^k) 형태로 정확히 표기해주세요.
													- **for 루프뿐만 아니라 while 루프도 모두 중첩(depth)에 포함**하여, 코드에 실제로 있는 루프 개수만큼 exponent를 세십시오.
													- 예) for-for-for ⇒ O(n³), for-for-while ⇒ O(n³), for-for-for-for-while ⇒ O(n⁵).
													
													- 코드 총평:
													- 각 문장은 한 탭(\t) 들여쓰기 + '- ' 로 시작.
													- 문장 끝에만 마침표를 붙이세요.
													
													- 조금 더 개선할 수 있는 방안:
													- 각 문장은 한 탭(\t) 들여쓰기 + '- ' 로 시작.
													- 문장 끝에만 마침표를 붙이세요.
													
													<오답일 경우>
													- 코드 총평:
													- 각 문장은 한 탭(\t) 들여쓰기 + '- ' 로 시작.
													- 문장 끝에만 마침표를 붙이세요.
													
													- 공부하면 좋은 키워드:
													1. 첫 번째 키워드
													2. 두 번째 키워드
													3. 세 번째 키워드
													… 필요한 만큼 번호를 늘려주세요.
													
													**주의사항**
													1. 절대 코드 전문이나 정답을 알려주지 마세요.
													2. 절대 코드의 일부분을 작성하지 마세요.
													3. 칭찬할 건 칭찬하되 지적할 건 냉정하게 지적하세요.
													4. 늘 존댓말을 사용하세요.
													5. 이모지는 절대 사용하지 마세요.
													6. 다른 형식으로 답변하면 안 됩니다.
													"""),
				Map.of("role", "user", "content", userPrompt)
			)
		);

		OpenAIResponse response = webClient.post()
			.uri("/v1/chat/completions")
			.header("Authorization", "Bearer " + openApiKey)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(OpenAIResponse.class)
			.timeout(Duration.ofSeconds(10))
			.retryWhen(
				Retry.backoff(3, Duration.ofSeconds(1))
					.maxBackoff(Duration.ofSeconds(5))
					.filter(ex -> ex instanceof WebClientResponseException
										 || ex instanceof TimeoutException)
					.onRetryExhaustedThrow((spec, signal) -> signal.failure())
			)
			.onErrorMap(WebClientResponseException.class,
				ex -> new CodeReviewException(CodeReviewExceptionCode.REVIEW_SERVER_ERROR))
			.onErrorMap(TimeoutException.class,
				ex -> new CodeReviewException(CodeReviewExceptionCode.REVIEW_TIMEOUT))
			.block();

		return Objects.requireNonNull(response).getReviewContent();
	}
}
