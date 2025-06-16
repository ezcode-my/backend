package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.entity.Reply;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReplyResponse", description = "Reply 조회 응답 DTO")
public record ReplyResponse(
	@Schema(description = "Reply 고유 ID", example = "456", requiredMode = REQUIRED)
	Long replyId,

	@Schema(description = "부모 Reply ID (없으면 null)", example = "123", nullable = true, requiredMode = NOT_REQUIRED)
	Long parentId,

	@Schema(description = "소속 Discussion ID", example = "123", requiredMode = REQUIRED)
	Long discussionId,

	@Schema(description = "작성자 정보", requiredMode = REQUIRED)
	SimpleUserInfoResponse userInfo,

	@Schema(description = "댓글 내용", example = "동의합니다!", requiredMode = REQUIRED)
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
