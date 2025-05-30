package org.ezcode.codetest.application.language;

import java.util.List;

import org.ezcode.codetest.domain.language.exception.LanguageException;
import org.ezcode.codetest.domain.language.exception.LanguageExceptionCode;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

	private final LanguageDomainService languageDomainService;

	public LanguageResponse createLanguage(LanguageRequest request) {

		if (languageDomainService.hasLanguage(request.name(), request.version())) {
			throw new LanguageException(LanguageExceptionCode.LANGUAGE_ALREADY_EXISTS);
		}

		Language language = languageDomainService.createLanguage(LanguageRequest.toEntity(request));

		return LanguageResponse.from(language);
	}

	public List<LanguageResponse> getLanguages() {
		return languageDomainService.getLanguages()
			.stream()
			.map(LanguageResponse::from)
			.toList();
	}

	public void deleteLanguage(Long languageId) {

		if (!languageDomainService.hasLanguage(languageId)) {
			throw new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND);
		}

		languageDomainService.deleteLanguage(languageId);
	}
}
