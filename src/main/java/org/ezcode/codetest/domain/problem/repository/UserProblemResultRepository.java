package org.ezcode.codetest.domain.problem.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.UserProblemResult;

public interface UserProblemResultRepository {
	Optional<UserProblemResult> findUserProblemResultByUserIdAndProblemId(Long userId, Long problemId);

	void saveUserProblemResult(UserProblemResult userProblemResult);

	void updateUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect);
}
