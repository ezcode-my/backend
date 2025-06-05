package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.problem.repository.UserProblemResultRepository;
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
}
