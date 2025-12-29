package org.ezcode.codetest.domain.draft.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.draft.model.entity.Draft;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

public interface DraftRepository {

	Draft save(Draft draft);

	Optional<Draft> findByUserAndProblemAndLanguage(User user, Problem problem, Language language);

	Draft saveAndFlush(Draft draft);

}
