package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.ProblemLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemLanguageJpaRepository extends JpaRepository<ProblemLanguage, Long> {
}
