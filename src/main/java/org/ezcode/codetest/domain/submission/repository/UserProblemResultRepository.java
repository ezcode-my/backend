package org.ezcode.codetest.domain.submission.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.springframework.data.repository.query.Param;


public interface UserProblemResultRepository {
    Optional<UserProblemResult> findUserProblemResultByUserIdAndProblemId(Long userId, Long problemId);

    UserProblemResult saveUserProblemResult(UserProblemResult userProblemResult);

    void updateUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect);

    List<Object[]> findScoresBetween(LocalDateTime start, LocalDateTime end);

    Optional<Integer> sumPointByUserId(@Param("userId") Long userId);

    List<DailyCorrectCount> countCorrectByUserGroupedByDate(Long userId);
}
