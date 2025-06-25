package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import static org.ezcode.codetest.domain.community.model.entity.QDiscussion.*;
import static org.ezcode.codetest.domain.community.model.entity.QDiscussionVote.*;
import static org.ezcode.codetest.domain.community.model.entity.QReply.*;

import java.util.List;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.dto.QDiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.user.model.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiscussionQueryOptimizedRepositoryImpl implements DiscussionQueryOptimizedRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<DiscussionQueryResult> findAllByProblemIdOptimized(Long problemId, String sortBy, Long currentUserId, Pageable pageable) {

		QUser user = QUser.user;

		Expression<Long> replyCount = reply.id.countDistinct();

		NumberExpression<Long> upvoteCount = new CaseBuilder()
			.when(discussionVote.voteType.eq(VoteType.UP)).then(discussionVote.id) // 'UP'일 때 vote의 id를 대상으로
			.otherwise((Long) null)
			.countDistinct();
		NumberExpression<Long> downvoteCount = new CaseBuilder()
			.when(discussionVote.voteType.eq(VoteType.DOWN)).then(discussionVote.id) // 'DOWN'일 때 vote의 id를 대상으로
			.otherwise((Long) null)
			.countDistinct();

		NumberExpression<Long> bestScore = upvoteCount.subtract(downvoteCount);
		Expression<VoteType> userVoteType;
		if (currentUserId != null) {
			// GROUP BY 때문에 집계 함수(max)로 감싸줌. 한 유저는 토론당 투표가 최대 1개이므로 max를 써도 무방.
			userVoteType = new CaseBuilder()
				.when(discussionVote.voter.id.eq(currentUserId)).then(discussionVote.voteType)
				.otherwise(Expressions.nullExpression(VoteType.class)).max();
		} else {
			userVoteType = Expressions.nullExpression(VoteType.class);
		}

		List<DiscussionQueryResult> results = jpaQueryFactory
			.select(new QDiscussionQueryResult(
				discussion.id,
				Projections.constructor(SimpleUserInfoResponse.class,
					user.id,
					user.nickname,
					user.tier,
					user.profileImageUrl
				),
				discussion.problem.id,
				discussion.content,
				discussion.createdAt,
				upvoteCount,
				downvoteCount,
				replyCount,
				userVoteType
			))
			.from(discussion)
			.join(user).on(discussion.user.id.eq(user.id))

			.leftJoin(discussionVote).on(discussionVote.discussion.eq(discussion))
			.leftJoin(reply).on(reply.discussion.eq(discussion))
			.where(discussion.problem.id.eq(problemId))
			.groupBy(discussion.id)
			.orderBy(getOrderSpecifier(sortBy, bestScore, upvoteCount))

			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(discussion.count())
			.from(discussion)
			.where(discussion.problem.id.eq(problemId));

		return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
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
