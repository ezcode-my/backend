package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;


import java.util.List;
import java.util.Set;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.model.entity.QUserProblemResult;
import org.springframework.stereotype.Repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserProblemResultQueryRepositoryImpl implements UserProblemResultQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DailyCorrectCount> countCorrectByUserGroupedByDate(Long userId) {

        QUserProblemResult upr = QUserProblemResult.userProblemResult;

        var date = Expressions.dateTemplate(java.sql.Date.class, "DATE({0})", upr.modifiedAt);
        NumberExpression<Long> countDistinctProblem = upr.problem.id.countDistinct();

        return queryFactory
            .from(upr)
            .where(
                upr.user.id.eq(userId),
                upr.isCorrect.eq(true)
            )
            .orderBy(date.asc())
            .transform(
                GroupBy.groupBy(date).list(
                    Projections.constructor(
                        DailyCorrectCount.class,
                        date,
                        countDistinctProblem,
                        GroupBy.set(upr.problem.id)
                    )
                )
            );
    }
}
