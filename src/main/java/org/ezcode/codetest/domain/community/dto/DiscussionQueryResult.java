package org.ezcode.codetest.domain.community.dto;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class DiscussionQueryResult {

	private final Long discussionId;

	private final SimpleUserInfoResponse userInfo;

	private final Long problemId;

	private final String content;

	private final LocalDateTime createdAt;

	private final Long upvoteCount;

	private final Long downvoteCount;

	private final Long replyCount;

	private final VoteType voteStatus;

	@QueryProjection
	public DiscussionQueryResult(
		Long discussionId,
		SimpleUserInfoResponse userInfo,
		Long problemId,
		String content,
		LocalDateTime createdAt,
		Long upvoteCount,
		Long downvoteCount,
		Long replyCount,
		VoteType voteType
	) {

		this.discussionId = discussionId;
		this.userInfo = userInfo;
		this.problemId = problemId;
		this.content = content;
		this.createdAt = createdAt;
		this.upvoteCount = upvoteCount;
		this.downvoteCount = downvoteCount;
		this.replyCount = replyCount;

		if (voteType == null) {
			this.voteStatus = VoteType.NONE;
		} else if (voteType == VoteType.UP) {
			this.voteStatus = VoteType.UP;
		} else {
			this.voteStatus = VoteType.DOWN;
		}
	}
}
