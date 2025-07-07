package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.entity.Discussion;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DiscussionResponse", description = "Discussion 응답 DTO, 목록 조회 시에만 추천 수, 비추천 수 등의 데이터가 포함됨")
public record DiscussionResponse(

	@Schema(description = "Discussion 고유 ID", example = "123", requiredMode = REQUIRED)
	Long discussionId,

	@Schema(description = "작성자 정보", requiredMode = REQUIRED)
	SimpleUserInfoResponse userInfo,

	@Schema(description = "관련 문제 ID", example = "45", requiredMode = REQUIRED)
	Long problemId,

	@Schema(description = "토론 내용", example = "이 문제는 이렇게 풀 수 있습니다...", requiredMode = REQUIRED)
	String content,

	@Schema(description = "생성 일시", example = "2025-06-25T14:30:00", requiredMode = REQUIRED)
	LocalDateTime createdAt,

	@Schema(description = "총 추천 수 (upvote)", example = "10")
	Long upvoteCount,

	@Schema(description = "총 비추천 수 (downvote)", example = "2")
	Long downvoteCount,

	@Schema(description = "총 댓글 수", example = "5")
	Long replyCount,

	@Schema(description = "현재 사용자의 추천 상태 (UP, DOWN, NONE)", example = "UP")
	VoteType voteStatus,
	
	@Schema(description = "로그인한 유저의 해당 토론글 작성 여부", example = "true")
	boolean isAuthor

) {

	public static DiscussionResponse fromEntity(Discussion discussion) {
		return new DiscussionResponse(
			discussion.getId(),
			SimpleUserInfoResponse.fromEntity(discussion.getUser()),
			discussion.getProblem().getId(),
			discussion.getContent(),
			discussion.getCreatedAt(),
			null,
			null,
			null,
			null,
			false
		);
	}

	public static DiscussionResponse from(DiscussionQueryResult result) {
		return new DiscussionResponse(
			result.getDiscussionId(),
			result.getUserInfo(),
			result.getProblemId(),
			result.getContent(),
			result.getCreatedAt(),
			result.getUpvoteCount(),
			result.getDownvoteCount(),
			result.getReplyCount(),
			result.getVoteStatus(),
			result.isAuthor()
		);
	}
}
