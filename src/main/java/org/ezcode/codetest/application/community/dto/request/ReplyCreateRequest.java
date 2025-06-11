package org.ezcode.codetest.application.community.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ReplyCreateRequest", description = "새로운 Reply 생성 요청 DTO")
public record ReplyCreateRequest(

	@Schema(description = "부모 Reply ID (대댓글인 경우)", example = "5", nullable = true)
	Long parentReplyId,

	@Schema(description = "댓글 내용", example = "좋은 의견입니다!", maxLength = 2000, requiredMode = REQUIRED)
	@NotBlank(message = "내용을 입력해야 합니다.")
	@Size(max = 2000, message = "내용은 최대 2000자까지 허용됩니다.")
	String content

) {

	public static Reply toEntity(
		Discussion discussion,
		User user,
		Reply parent,
		ReplyCreateRequest request
	) {
		return Reply.builder()
			.discussion(discussion)
			.user(user)
			.parent(parent)
			.content(request.content())
			.build();
	}
}
