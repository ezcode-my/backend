package org.ezcode.codetest.application.submission.service;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.language.LanguageCreateRequest;
import org.ezcode.codetest.application.submission.dto.request.language.LanguageUpdateRequest;
import org.ezcode.codetest.application.submission.dto.response.language.LanguageResponse;
import org.ezcode.codetest.domain.submission.service.LanguageDomainService;
import org.ezcode.codetest.domain.submission.model.entity.Language;
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
