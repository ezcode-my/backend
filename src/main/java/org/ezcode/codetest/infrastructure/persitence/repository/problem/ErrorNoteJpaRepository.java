package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.ErrorNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorNoteJpaRepository extends JpaRepository<ErrorNote, Long> {
}
