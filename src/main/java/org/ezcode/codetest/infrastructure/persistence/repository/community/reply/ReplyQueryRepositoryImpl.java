package org.ezcode.codetest.infrastructure.persistence.repository.community.reply;

import static org.ezcode.codetest.domain.community.model.entity.QReply.*;
import static org.ezcode.codetest.domain.user.model.entity.QUser.*;
import static org.ezcode.codetest.domain.community.model.entity.QReplyVote.*;

import java.util.List;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.QReplyQueryResult;
import org.ezcode.codetest.domain.community.dto.ReplyQueryResult;
import org.ezcode.codetest.domain.community.model.entity.QReply;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReplyQueryRepositoryImpl implements ReplyQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	// 댓글 목록 조회
	@Override
	public Page<ReplyQueryResult> findRepliesByDiscussionId(Long discussionId, Long currentUserId, Pageable pageable) {

		BooleanExpression condition = reply.discussion.id.eq(discussionId)
			.and(reply.parent.isNull());

		List<ReplyQueryResult> results = findRepliesByCondition(condition, currentUserId, pageable);
		Long total = countByDiscussionId(discussionId);

		return new PageImpl<>(results, pageable, total);
	}

	// 대댓글 목록 조회
	@Override
	public Page<ReplyQueryResult> findRepliesByParentId(Long parentId, Long currentUserId, Pageable pageable) {

		BooleanExpression condition = reply.parent.id.eq(parentId);

		List<ReplyQueryResult> results = findRepliesByCondition(condition, currentUserId, pageable);
		Long total = countByParentId(parentId);

		return new PageImpl<>(results, pageable, total);
	}

	private List<ReplyQueryResult> findRepliesByCondition(BooleanExpression condition, Long currentUserId, Pageable pageable) {
		QReply childReply = new QReply("childReply");

		NumberExpression<Long> upvoteCount = getVoteCount(VoteType.UP);
		NumberExpression<Long> downvoteCount = getVoteCount(VoteType.DOWN);

		Expression<Long> childReplyCount = childReply.id.count();

		Expression<VoteType> userVoteType;
		BooleanExpression isAuthor;
		if (currentUserId != null) {
			userVoteType = new CaseBuilder()
				.when(replyVote.voter.id.eq(currentUserId)).then(replyVote.voteType)
				.otherwise(Expressions.nullExpression(VoteType.class)).max();
			isAuthor = reply.user.id.eq(currentUserId);
		} else {
			userVoteType = Expressions.nullExpression(VoteType.class);
			isAuthor = Expressions.FALSE;
		}

		return jpaQueryFactory
			.select(new QReplyQueryResult(
				reply.id,
				Projections.constructor(SimpleUserInfoResponse.class,
					user.id,
					user.nickname,
					user.tier,
					user.profileImageUrl
				),
				reply.parent.id,
				reply.discussion.id,
				reply.content,
				reply.createdAt,
				upvoteCount,
				downvoteCount,
				childReplyCount,
				userVoteType,
				isAuthor
			))
			.from(reply)
			.join(reply.user, user)
			.leftJoin(replyVote).on(replyVote.reply.eq(reply))
			.leftJoin(childReply).on(childReply.parent.eq(reply))
			.where(condition.and(reply.isDeleted.isFalse()))
			.groupBy(reply.id)
			.orderBy(reply.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public Long countByDiscussionId(Long discussionId) {
		return jpaQueryFactory
			.select(reply.count())
			.from(reply)
			.where(reply.discussion.id.eq(discussionId).and(reply.parent.isNull()))
			.fetchOne();
	}

	@Override
	public Long countByParentId(Long parentId) {
		return jpaQueryFactory
			.select(reply.count())
			.from(reply)
			.where(reply.parent.id.eq(parentId))
			.fetchOne();
	}

	private NumberExpression<Long> getVoteCount(VoteType voteType) {
		return new CaseBuilder()
			.when(replyVote.voteType.eq(voteType)).then(replyVote.id)
			.otherwise((Long) null)
			.countDistinct();
	}
}
