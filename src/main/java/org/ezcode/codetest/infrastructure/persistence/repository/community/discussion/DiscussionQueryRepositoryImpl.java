package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import static org.ezcode.codetest.domain.community.model.entity.QDiscussion.*;
import static org.ezcode.codetest.domain.community.model.entity.QDiscussionVote.*;
import static org.ezcode.codetest.domain.community.model.entity.QReply.*;
import static org.ezcode.codetest.domain.user.model.entity.QUser.*;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.dto.QDiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiscussionQueryRepositoryImpl implements DiscussionQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Long> findDiscussionIdsByProblemId(Long problemId, String sortBy, Pageable pageable) {

		NumberExpression<Long> upvoteCount = getVoteCount(VoteType.UP);
		NumberExpression<Long> downvoteCount = getVoteCount(VoteType.DOWN);

		NumberExpression<Long> bestScore = upvoteCount.subtract(downvoteCount);

		return jpaQueryFactory
			.select(discussion.id)
			.from(discussion)
			.leftJoin(discussionVote).on(discussionVote.discussion.eq(discussion))
			.where(discussion.problem.id.eq(problemId).and(discussion.isDeleted.isFalse()))
			.groupBy(discussion.id)
			.orderBy(getOrderSpecifier(sortBy, bestScore, upvoteCount))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<DiscussionQueryResult> findDiscussionsByIds(List<Long> discussionIds, Long currentUserId) {

		if (discussionIds == null || discussionIds.isEmpty()) {
			return new ArrayList<>();
		}

		NumberExpression<Long> upvoteCount = getVoteCount(VoteType.UP);
		NumberExpression<Long> downvoteCount = getVoteCount(VoteType.DOWN);

		Expression<Long> replyCount = reply.id.countDistinct();

		Expression<VoteType> userVoteType;
		BooleanExpression isAuthor;
		if (currentUserId != null) {
			userVoteType = new CaseBuilder()
				.when(discussionVote.voter.id.eq(currentUserId)).then(discussionVote.voteType)
				.otherwise(Expressions.nullExpression(VoteType.class)).max();
			isAuthor = discussion.user.id.eq(currentUserId);
		} else {
			userVoteType = Expressions.nullExpression(VoteType.class);
			isAuthor = Expressions.FALSE;
		}

		return jpaQueryFactory
			.select(new QDiscussionQueryResult(
				discussion.id,
				Projections.constructor(SimpleUserInfoResponse.class,
					user.id,
					user.nickname,
					user.tier,
					user.profileImageUrl
				),
				discussion.problem.id,
				discussion.language.id,
				discussion.content,
				discussion.createdAt,
				upvoteCount,
				downvoteCount,
				replyCount,
				userVoteType,
				isAuthor
			))
            .from(discussion)
			.join(discussion.user, user)
			.leftJoin(discussionVote).on(discussionVote.discussion.eq(discussion))
			.leftJoin(reply).on(reply.discussion.eq(discussion))
			.where(discussion.id.in(discussionIds))
			.groupBy(
				discussion.id,
				user.id,
				user.nickname,
				user.tier,
				user.profileImageUrl,
				discussion.problem.id,
				discussion.content,
				discussion.createdAt
			)
            .fetch();
	}

	@Override
	public Long countByProblemId(Long problemId) {

		Long count = jpaQueryFactory
			.select(discussion.count())
			.from(discussion)
			.where(discussion.problem.id.eq(problemId).and(discussion.isDeleted.isFalse()))
			.fetchOne();

		return count != null ? count : 0L;
	}


	private NumberExpression<Long> getVoteCount(VoteType voteType) {
		return new CaseBuilder()
			.when(discussionVote.voteType.eq(voteType)).then(discussionVote.id)
			.otherwise((Long) null)
			.countDistinct();
	}

	private OrderSpecifier<?>[] getOrderSpecifier(String sort, NumberExpression<Long> bestScore, Expression<Long> upvoteCount) {
		OrderSpecifier<?> primarySort;

		switch (sort.toLowerCase()) {
			case "best":
				primarySort = new OrderSpecifier<>(Order.DESC, bestScore);
				break;
			case "upvote":
				primarySort = new OrderSpecifier<>(Order.DESC, upvoteCount);
				break;
			default:
				return new OrderSpecifier<?>[]{ new OrderSpecifier<>(Order.DESC, discussion.id) };
		}

		OrderSpecifier<?> secondarySort = new OrderSpecifier<>(Order.DESC, discussion.id);

		return new OrderSpecifier<?>[] { primarySort, secondarySort };
	}
}
