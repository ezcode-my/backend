package org.ezcode.codetest.infrastructure.openai;

import org.springframework.stereotype.Component;

@Component
class OpenAIResponseValidator {
	protected boolean isValidFormat(String content, boolean isCorrect) {
		if (content == null) return false;

		if (isCorrect) {
			return content.contains("시간 복잡도:") &&
				content.contains("코드 총평:") &&
				content.contains("조금 더 개선할 수 있는 방안:");
		}

		return content.contains("코드 총평:") &&
			content.contains("공부하면 좋은 키워드:");
	}
}
