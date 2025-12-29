package org.ezcode.codetest.application.draft.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DraftSaveRequest(
	@NotNull(message = "문제 ID는 필수입니다.")
	Long problemId,

	@NotNull(message = "언어 ID는 필수입니다.")
	Long languageId,

	@NotNull(message = "코드는 필수입니다.")
	@Size(max = 100000, message = "코드는 최대 100,000자까지 입력 가능합니다.")
	String code,

	Long version  // 새 엔티티는 null일 수 있으므로 @NotNull 제외
) {
}
