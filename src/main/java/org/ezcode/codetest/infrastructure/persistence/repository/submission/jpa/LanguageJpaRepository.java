package org.ezcode.codetest.infrastructure.persistence.repository.submission.jpa;

import org.ezcode.codetest.domain.language.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageJpaRepository extends JpaRepository<Language, Long> {
    boolean existsByNameAndVersion(String name, String version);
}
