package org.ezcode.codetest.application.community.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "DiscussionModifyRequest", description = "기존 Discussion 수정 요청 DTO")
public record DiscussionModifyRequest(

	@Schema(description = "문제에 사용할 언어 ID", example = "1", requiredMode = REQUIRED)
	@NotNull(message = "languageId는 필수값입니다.")
	Long languageId,

	@Schema(description = "수정할 토론 내용", example = "내용을 이렇게 바꿨습니다...", maxLength = 2000, requiredMode = REQUIRED)
	@NotBlank(message = "내용을 입력해야 합니다.")
	@Size(max = 2000, message = "내용은 최대 2000자까지 허용됩니다.")
	String content

) {
}
