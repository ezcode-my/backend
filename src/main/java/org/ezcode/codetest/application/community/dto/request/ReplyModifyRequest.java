package org.ezcode.codetest.application.community.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ReplyModifyRequest", description = "기존 Reply 수정 요청 DTO")
public record ReplyModifyRequest(

	@Schema(description = "수정할 댓글 내용", example = "내용을 이렇게 변경합니다.", maxLength = 2000, requiredMode = REQUIRED)
	@NotBlank(message = "내용을 입력해야 합니다.")
	@Size(max = 2000, message = "내용은 최대 2000자까지 허용됩니다.")
	String content

) {
}
