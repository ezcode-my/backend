package org.ezcode.codetest.infrastructure.openai;

import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.springframework.stereotype.Component;

@Component
class OpenAIMessageBuilder {
	protected String buildPrompt(ReviewPayload request) {
		String status = request.isCorrect() ? "정답" : "오답";
		return """
        문제: %s

        언어: %s
        정답 여부: %s

        ```%s
        %s
        ```
        
        """.formatted(
			request.problemDescription(),
			request.languageName(),
			status,
			request.languageName().toLowerCase(),
			request.sourceCode()
		);
	}

	protected String buildErrorMessage() {
		return "현재 리뷰 생성에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.";
	}
}
