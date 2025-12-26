package org.ezcode.codetest.infrastructure.persistence.repository.draft;

import java.util.Optional;

import org.ezcode.codetest.domain.draft.model.entity.Draft;
import org.ezcode.codetest.domain.draft.repository.DraftRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DraftRepositoryImpl implements DraftRepository {

	private final DraftJpaRepository draftJpaRepository;

	@Override
	public Draft save(Draft draft) {

		return draftJpaRepository.save(draft);
	}

	@Override
	public Optional<Draft> findByUserAndProblemAndLanguage(User user, Problem problem, Language language) {

		return draftJpaRepository.findByUserAndProblemAndLanguage(user, problem, language);
	}

	@Override
	public Draft saveAndFlush(Draft draft) {

		return draftJpaRepository.saveAndFlush(draft);
	}
}
