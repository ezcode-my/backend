package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.entity.UserProblemResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProblemResultJpaRepository extends JpaRepository<UserProblemResult, Long> {
	Optional<UserProblemResult> findByUserIdAndProblemId(Long userId, Long problemId);
}
