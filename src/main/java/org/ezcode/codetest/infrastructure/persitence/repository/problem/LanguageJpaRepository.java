package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageJpaRepository extends JpaRepository<Language, Long> {
}
