package org.ezcode.codetest.infrastructure.persistence.repository.community.discussion;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.dto.DiscussionQueryResult;
import org.ezcode.codetest.domain.community.dto.QDiscussionQueryResult;
import org.ezcode.codetest.domain.community.model.entity.QDiscussionVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.user.model.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static org.ezcode.codetest.domain.community.model.entity.QDiscussion.discussion;
import static org.ezcode.codetest.domain.community.model.entity.QDiscussionVote.discussionVote;
import static org.ezcode.codetest.domain.community.model.entity.QReply.reply;

import java.util.List;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberOperation;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DiscussionQueryRepositoryImpl implements DiscussionQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<DiscussionQueryResult> findAllByProblemId(Long problemId, String sortBy, Long currentUserId, Pageable pageable) {

		QUser user = discussion.user;

		JPQLQuery<Long> upvoteCount = getVoteCount(VoteType.UP);
		JPQLQuery<Long> downvoteCount = getVoteCount(VoteType.DOWN);

		NumberOperation<Long> bestScore = Expressions.numberOperation(
			Long.class,
			Ops.SUB,
			upvoteCount,
			downvoteCount
		);

		JPQLQuery<Long> replyCount = JPAExpressions
			.select(reply.count())
			.from(reply)
			.where(reply.discussion.eq(discussion));

		Expression<VoteType> userVoteType;

		if (currentUserId != null) {
			QDiscussionVote discussionVoteForUser = new QDiscussionVote("discussionVoteForUser");

			userVoteType = JPAExpressions
				.select(discussionVoteForUser.voteType)
				.from(discussionVoteForUser)
				.where(discussionVoteForUser.discussion.eq(discussion)
					.and(discussionVoteForUser.voter.id.eq(currentUserId)));
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
			.where(discussion.problem.id.eq(problemId))
			.orderBy(getOrderSpecifier(sortBy, bestScore))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(discussion.count())
			.where(discussion.problem.id.eq(problemId));

		return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
	}

	private JPQLQuery<Long> getVoteCount(VoteType voteType) {

		return JPAExpressions
			.select(discussionVote.count())
			.from(discussionVote)
			.where(discussionVote.discussion.eq(discussion)
				.and(discussionVote.voteType.eq(voteType)));
	}

	private OrderSpecifier<?>[] getOrderSpecifier(String sort, NumberExpression<Long> bestScore) {
		OrderSpecifier<?> primarySort;

		switch (sort.toLowerCase()) {
			case "best":
				primarySort = new OrderSpecifier<>(Order.DESC, bestScore);
				break;
			case "upvote":
				primarySort = new OrderSpecifier<>(Order.DESC, getVoteCount(VoteType.UP));
				break;
			default:
				return new OrderSpecifier<?>[]{ new OrderSpecifier<>(Order.DESC, discussion.id) };
		}

		OrderSpecifier<?> secondarySort = new OrderSpecifier<>(Order.DESC, discussion.id);

		return new OrderSpecifier<?>[] { primarySort, secondarySort };
	}
}
