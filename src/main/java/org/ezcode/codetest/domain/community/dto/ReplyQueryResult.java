package org.ezcode.codetest.domain.community.dto;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class ReplyQueryResult {

	private final Long replyId;

	private final SimpleUserInfoResponse userInfo;

	private final Long parentReplyId;

	private final Long discussionId;

	private final String content;

	private final LocalDateTime createdAt;

	private final Long upvoteCount;

	private final Long downvoteCount;

	private final Long childReplyCount;

	private final VoteType voteStatus;

	private final boolean isAuthor;

	@QueryProjection
	public ReplyQueryResult(
		Long replyId,
		SimpleUserInfoResponse userInfo,
		Long parentReplyId,
		Long discussionId,
		String content,
		LocalDateTime createdAt,
		Long upvoteCount,
		Long downvoteCount,
		Long childReplyCount,
		VoteType voteType,
		boolean isAuthor
	) {

		this.replyId = replyId;
		this.userInfo = userInfo;
		this.parentReplyId = parentReplyId;
		this.discussionId = discussionId;
		this.content = content;
		this.createdAt = createdAt;
		this.upvoteCount = upvoteCount;
		this.downvoteCount = downvoteCount;
		this.childReplyCount = childReplyCount;

		if (voteType == null) {
			this.voteStatus = VoteType.NONE;
		} else if (voteType == VoteType.UP) {
			this.voteStatus = VoteType.UP;
		} else {
			this.voteStatus = VoteType.DOWN;
		}

		this.isAuthor = isAuthor;
	}
}
