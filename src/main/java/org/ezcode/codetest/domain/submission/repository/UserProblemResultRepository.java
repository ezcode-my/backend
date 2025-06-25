package org.ezcode.codetest.domain.submission.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserProblemResultRepository {
    Optional<UserProblemResult> findUserProblemResultByUserIdAndProblemId(Long userId, Long problemId);

    UserProblemResult saveUserProblemResult(UserProblemResult userProblemResult);

    UserProblemResult updateUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect);

    @Query("""
            SELECT upr.user.id, SUM(p.score)
            FROM UserProblemResult upr
            JOIN upr.problem p
            WHERE upr.isCorrect = true
              AND (:start IS NULL OR upr.createdAt >= :start)
              AND (:end IS NULL OR upr.createdAt < :end)
            GROUP BY upr.user.id
        """)
    List<Object[]> findScoresBetween(LocalDateTime start, LocalDateTime end);

    Optional<Integer> sumPointByUserId(@Param("userId") Long userId);
}
