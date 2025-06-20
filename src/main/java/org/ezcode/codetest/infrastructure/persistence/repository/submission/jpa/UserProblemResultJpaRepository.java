package org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProblemResultJpaRepository extends JpaRepository<UserProblemResult, Long> {
    Optional<UserProblemResult> findByUserIdAndProblemId(Long userId, Long problemId);

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

}
