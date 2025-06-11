package org.ezcode.codetest.application.community.dto.response;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.Reply;

public record ReplyResponse(

	Long replyId,

	Long parentId,

	Long discussionId,

	SimpleUserInfoResponse userInfo,

	String content

) {

	public static ReplyResponse fromEntity(Reply reply) {
		Long parentId = (reply.getParent() != null)
			? reply.getParent().getId()
			: null;

		return new ReplyResponse(
			reply.getId(),
			parentId,
			reply.getDiscussion().getId(),
			SimpleUserInfoResponse.fromEntity(reply.getUser()),
			reply.getContent()
		);
	}
}
