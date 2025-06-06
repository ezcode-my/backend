package org.ezcode.codetest.application.submission.dto.request.language;

import jakarta.validation.constraints.NotNull;

public record LanguageUpdateRequest(

	@NotNull(message = "Judge0 아이디는 필수 입력 값입니다.")
	Long judge0Id

) {
}
