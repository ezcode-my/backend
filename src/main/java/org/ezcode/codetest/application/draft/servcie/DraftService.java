package org.ezcode.codetest.application.draft.servcie;

import java.util.Optional;

import org.ezcode.codetest.application.draft.dto.request.DraftSaveRequest;
import org.ezcode.codetest.application.draft.dto.response.DraftResponse;
import org.ezcode.codetest.domain.draft.exception.DraftException;
import org.ezcode.codetest.domain.draft.exception.DraftExceptionCode;
import org.ezcode.codetest.domain.draft.model.entity.Draft;
import org.ezcode.codetest.domain.draft.service.DraftDomainService;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftService {

	private final DraftDomainService draftDomainService;

	private final UserDomainService userDomainService;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;

	@Transactional
	public DraftResponse autoSave(Long userId, DraftSaveRequest request) {

		User user = userDomainService.getUserById(userId);
		Problem problem = problemDomainService.getProblem(request.problemId());
		Language language = languageDomainService.getLanguage(request.languageId());

		Draft draft;
		try {
			draft = draftDomainService.autoSave(
				user,
				problem,
				language,
				request.code(),
				request.version()
			);
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new DraftException(DraftExceptionCode.DRAFT_VERSION_CONFLICT);
		}

		return DraftResponse.from(draft);
	}

	@Transactional(readOnly = true)
	public Optional<DraftResponse> getDraft(Long userId, Long problemId, Long languageId) {

		User user = userDomainService.getUserById(userId);
		Problem problem = problemDomainService.getProblem(problemId);
		Language language = languageDomainService.getLanguage(languageId);

		return draftDomainService.getDraft(user, problem, language)
			.map(DraftResponse::from);
	}
}
