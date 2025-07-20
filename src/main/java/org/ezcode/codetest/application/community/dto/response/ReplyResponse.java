package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.ReplyQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Reply;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReplyResponse", description = "Reply 조회 응답 DTO")
public record ReplyResponse(

	@Schema(description = "Reply 고유 ID", example = "456", requiredMode = REQUIRED)
	Long replyId,

	@Schema(description = "부모 Reply ID (없으면 null)", example = "123", nullable = true, requiredMode = NOT_REQUIRED)
	Long parentReplyId,

	@Schema(description = "소속 Discussion ID", example = "123", requiredMode = REQUIRED)
	Long discussionId,

	@Schema(description = "작성자 정보", requiredMode = REQUIRED)
	SimpleUserInfoResponse userInfo,

	@Schema(description = "댓글 내용", example = "동의합니다!", requiredMode = REQUIRED)
	String content,

	@Schema(description = "생성 일시", example = "2025-06-25T14:30:00", requiredMode = REQUIRED)
	LocalDateTime createdAt,

	@Schema(description = "총 추천 수 (upvote)", example = "10")
	Long upvoteCount,

	@Schema(description = "총 비추천 수 (downvote)", example = "2")
	Long downvoteCount,

	@Schema(description = "총 댓글 수", example = "5")
	Long childReplyCount,

	@Schema(description = "현재 사용자의 추천 상태 (UP, DOWN, NONE)", example = "UP")
	VoteType voteStatus,

	@Schema(description = "로그인한 유저의 해당 토론글 작성 여부", example = "true")
	boolean isAuthor
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
			reply.getContent(),
			reply.getCreatedAt(),
			null,
			null,
			null,
			null,
			false
		);
	}

	public static ReplyResponse from(ReplyQueryResult result) {

		return new ReplyResponse(
			result.getReplyId(),
			result.getParentReplyId(),
			result.getDiscussionId(),
			result.getUserInfo(),
			result.getContent(),
			result.getCreatedAt(),
			result.getUpvoteCount(),
			result.getDownvoteCount(),
			result.getChildReplyCount(),
			result.getVoteStatus(),
			result.isAuthor()
		);
	}
}
