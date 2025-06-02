package org.ezcode.codetest.application.language;

import java.util.List;

import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

	private final LanguageDomainService languageDomainService;

	@Transactional
	public LanguageResponse createLanguage(LanguageCreateRequest request) {

		languageDomainService.validateLanguageNotDuplicated(request.name(), request.version());

		Language language = languageDomainService.createLanguage(LanguageCreateRequest.toEntity(request));

		return LanguageResponse.from(language);
	}

	@Transactional(readOnly = true)
	public List<LanguageResponse> getLanguages() {
		return languageDomainService.getLanguages()
			.stream()
			.map(LanguageResponse::from)
			.toList();
	}

	@Transactional
	public LanguageResponse modifyLanguage(Long languageId, LanguageUpdateRequest request) {
		Language language = languageDomainService.getLanguage(languageId);
		languageDomainService.modifyLanguage(language, request.judge0Id());

		return LanguageResponse.from(language);
	}

	@Transactional
	public void removeLanguage(Long languageId) {

		languageDomainService.validateLanguageExists(languageId);
		languageDomainService.removeLanguage(languageId);
	}
}
