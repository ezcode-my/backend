package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;


import java.util.List;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.model.entity.QUserProblemResult;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
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

        return queryFactory
            .select(Projections.constructor(DailyCorrectCount.class,
                date,
                upr.count().intValue()
            ))
            .from(upr)
            .where(
                upr.user.id.eq(userId),
                upr.isCorrect.eq(true)
            )
            .groupBy(date)
            .orderBy(date.asc())
            .fetch();
    }
}
