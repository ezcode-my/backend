package org.ezcode.codetest.application.community.dto.request;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.community.model.Reply;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReplyCreateRequest(

	// nullable
	Long parentReplyId,

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
			.content(request.content)
			.build();
	}
}
