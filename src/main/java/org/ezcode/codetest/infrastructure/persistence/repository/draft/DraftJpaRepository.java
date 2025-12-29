package org.ezcode.codetest.infrastructure.persistence.repository.draft;

import java.util.Optional;

import org.ezcode.codetest.domain.draft.model.entity.Draft;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftJpaRepository extends JpaRepository<Draft, Long> {

	Optional<Draft> findByUserAndProblemAndLanguage(User user, Problem problem, Language language);
}
