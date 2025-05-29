package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemJpaRepository extends JpaRepository<Problem, Long> {
}
