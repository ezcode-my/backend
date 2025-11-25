package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;


import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.model.entity.QUserProblemResult;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProblemResultQueryRepositoryImpl implements UserProblemResultQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DailyCorrectCount> countCorrectByUserGroupedByDate(Long userId) {

        QUserProblemResult upr = QUserProblemResult.userProblemResult;

        var date = Expressions.dateTemplate(Date.class, "DATE({0})", upr.modifiedAt);

        var results = queryFactory
            .select(date, upr.problem.id)
            .from(upr)
            .where(
                upr.user.id.eq(userId),
                upr.isCorrect.eq(true)
            )
            .orderBy(date.asc())
            .fetch();

        Map<LocalDate, Set<Long>> problemIdsByDate = results.stream()
            .collect(Collectors.groupingBy(
                tuple -> Objects.requireNonNull(tuple.get(0, Date.class)).toLocalDate(),
                LinkedHashMap::new,
                Collectors.mapping(
                    tuple -> tuple.get(1, Long.class),
                    Collectors.toSet()
                )
            ));

        return problemIdsByDate.entrySet().stream()
            .map(entry -> new DailyCorrectCount(
                entry.getKey(),
                entry.getValue().size(),
                entry.getValue()
            ))
            .collect(Collectors.toList());
    }
}
