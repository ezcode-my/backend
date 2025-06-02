package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.ProblemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemImageJpaRepository extends JpaRepository<ProblemImage, Long> {
}
