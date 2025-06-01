package org.ezcode.codetest.domain.problem.service;

import java.util.List;

import org.ezcode.codetest.domain.problem.exception.LanguageException;
import org.ezcode.codetest.domain.problem.exception.code.LanguageExceptionCode;
import org.ezcode.codetest.domain.problem.repository.LanguageRepository;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageDomainService {

	private final LanguageRepository languageRepository;

	public void validateLanguageExists(Long languageId) {
		if (!languageRepository.existsById(languageId)) {
			throw new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND);
		}
	}

	public void validateLanguageNotDuplicated(String name, String version) {
		if (languageRepository.existsByNameAndVersion(name, version)) {
			throw new LanguageException(LanguageExceptionCode.LANGUAGE_ALREADY_EXISTS);
		}
	}

	public Language createLanguage(Language language) {
		return languageRepository.saveLanguage(language);
	}

	public Language getLanguage(Long languageId) {
		return languageRepository.findLanguageById(languageId)
			.orElseThrow(() -> new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND));
	}

	public List<Language> getLanguages() {
		return languageRepository.findLanguages();
	}

	public void modifyLanguage(Language language, Long judge0Id) {
		languageRepository.updateLanguage(language, judge0Id);
	}

	public void deleteLanguage(Long languageId) {
		languageRepository.deleteLanguage(languageId);
	}

}
