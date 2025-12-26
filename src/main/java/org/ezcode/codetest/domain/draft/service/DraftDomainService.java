package org.ezcode.codetest.domain.draft.service;

import java.util.Optional;

import org.ezcode.codetest.domain.draft.exception.DraftException;
import org.ezcode.codetest.domain.draft.exception.DraftExceptionCode;
import org.ezcode.codetest.domain.draft.model.entity.Draft;
import org.ezcode.codetest.domain.draft.repository.DraftRepository;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftDomainService {

	private final DraftRepository draftRepository;

	public Draft autoSave(User user, Problem problem, Language language, String code, Long version) {

		Optional<Draft> existingDraftOpt = draftRepository.findByUserAndProblemAndLanguage(user, problem, language);

		if (existingDraftOpt.isPresent()) {
			Draft draft = existingDraftOpt.get();

			// 명시적 버전 검증: 클라이언트가 보낸 version과 DB의 version이 일치해야 함
			if (version != null && !draft.getVersion().equals(version)) {
				throw new DraftException(DraftExceptionCode.DRAFT_VERSION_CONFLICT);
			}

			draft.updateCode(code);
			draftRepository.saveAndFlush(draft);

			return draft;
		} else {
			// 새 엔티티인 경우
			Draft draft = Draft.builder()
				.user(user)
				.problem(problem)
				.language(language)
				.code(code)
				.build();

			return draftRepository.saveAndFlush(draft);
		}
	}

	public Draft getDraft(User user, Problem problem, Language language) {

		return draftRepository.findByUserAndProblemAndLanguage(user, problem, language)
			.orElseThrow(() -> new DraftException(DraftExceptionCode.DRAFT_NOT_FOUND));
	}
}
