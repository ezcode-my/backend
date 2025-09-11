package org.ezcode.codetest.infrastructure.persistence.repository.submission.query;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.model.entity.QUserProblemResult;
import org.springframework.stereotype.Repository;
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

        var date = Expressions.dateTemplate(LocalDate.class, "DATE({0})", upr.modifiedAt);
        var results = queryFactory
            .select(
                date,
                upr.problem.id.countDistinct(),
                upr.problem.id
            )
            .from(upr)
            .where(
                upr.user.id.eq(userId),
                upr.isCorrect.eq(true)
            )
            .orderBy(date.asc())
            .fetch();

        // 결과를 수동으로 그룹화해야함
        Map<LocalDate, Set<Long>> problemIdsByDate = new LinkedHashMap<>();
        Map<LocalDate, Long> countsByDate = new LinkedHashMap<>();
        
        for (var result : results) {
            LocalDate resultDate = result.get(0, LocalDate.class);
            Long problemId = result.get(2, Long.class);
            
            problemIdsByDate.computeIfAbsent(resultDate, k -> new HashSet<>()).add(problemId);
            countsByDate.put(resultDate, (long) problemIdsByDate.get(resultDate).size());
        }
        
        return problemIdsByDate.entrySet().stream()
            .map(entry -> new DailyCorrectCount(
                entry.getKey(),
                countsByDate.get(entry.getKey()),
                entry.getValue()
            ))
            .collect(Collectors.toList());
    }
}
