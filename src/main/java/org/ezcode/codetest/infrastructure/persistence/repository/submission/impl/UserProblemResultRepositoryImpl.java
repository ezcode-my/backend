package org.ezcode.codetest.infrastructure.persistence.repository.submission.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa.UserProblemResultJpaRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserProblemResultRepositoryImpl implements UserProblemResultRepository {

    private final UserProblemResultJpaRepository userProblemResultJpaRepository;

    @Override
    public Optional<UserProblemResult> findUserProblemResultByUserIdAndProblemId(Long userId, Long problemId) {
        return userProblemResultJpaRepository.findByUserIdAndProblemId(userId, problemId);
    }

    @Override
    public void saveUserProblemResult(UserProblemResult userProblemResult) {
        userProblemResultJpaRepository.save(userProblemResult);
    }

    @Override
    public void updateUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect) {
        userProblemResult.updateResult(isCorrect);
    }

    @Override
    public List<Object[]> findScoresBetween(LocalDateTime start, LocalDateTime end) {
        return userProblemResultJpaRepository.findScoresBetween(start, end);
    }

    @Override
    public Optional<Integer> sumPointByUserId(Long userId) {
        return userProblemResultJpaRepository.sumScoreByUserId(userId);
    }

}
