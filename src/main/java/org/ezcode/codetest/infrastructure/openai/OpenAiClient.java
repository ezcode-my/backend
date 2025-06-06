package org.ezcode.codetest.infrastructure.openai;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.model.OpenAiResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAiClient implements ReviewClient {

	@Value("${OPEN_API_URL}")
	private String openApiUrl;

	@Value("${OPEN_API_KEY}")
	private String openApiKey;
	private WebClient webClient;


	@PostConstruct
	private void init() {
		this.webClient = WebClient.create(openApiUrl);
	}

	@Override
	public ReviewResult requestReview(ReviewPayload request) {
		String userPrompt = buildPrompt(request);

		Map<String, Object> requestBody = Map.of(
			"model", "gpt-3.5-turbo",
			"messages", List.of(
				Map.of("role", "system", "content", "코딩 테스트 사이트의 코드 리뷰를 담당하는 역할을 해주세요."),
				Map.of("role", "user", "content", userPrompt)
			)
		);

		return webClient.post()
			.uri("/v1/chat/completions")
			.header("Authorization", "Bearer " + openApiKey)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(OpenAiResponse.class)
			.map(response -> new ReviewResult(response.getReviewContent()))
			.block();
	}

	private String buildPrompt(ReviewPayload request) {
		String status = request.isCorrect() ? "정답" : "오답";
		return """
			문제: %s
			
			아래는 %s 언어로 작성된 소스코드입니다.
			사용자가 제출한 코드이고, %s 입니다.
			
			```
			%s
			```
			- 정답일 경우: 시간 복잡도와 가독성, 더 나은 방법이 있다면 조언을 주세요. (코드를 보여주는 것 제외)
			- 오답일 경우: 오답 코드 부분과 오답 원인과 관련된 키워드 (예: 메서드 이름, 알고리즘 종류, 자료구조 등)를 알려주세요.
			절대 정답을 알려주지 마세요. 사용자의 학습에 방해가 됩니다. 늘 존댓말을 쓰세요.
			단호하게 말해도 좋으니 학습에 도움이 되는 방향으로 냉정하게 평가해.
			""".formatted(request.problemDescription(), request.languageName(), status, request.sourceCode());
	}
}
