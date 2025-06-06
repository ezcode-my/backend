package org.ezcode.codetest.application.submission.dto.request.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CodeReviewRequest(

	@NotNull(message = "언어 번호는 필수 입력 값입니다.")
	Long languageId,

	@NotBlank(message = "소스 코드는 필수 입력 값입니다.")
	String sourceCode,

	@NotNull(message = "정답 여부는 필수 입력 값입니다.")
	Boolean isCorrect

) {
}
