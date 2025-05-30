package org.ezcode.codetest.domain.language.service;

import java.util.List;

import org.ezcode.codetest.domain.language.repository.LanguageRepository;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageDomainService {

	private final LanguageRepository languageRepository;

	public boolean hasLanguage(Long languageId) {
		return languageRepository.existsById(languageId);
	}

	public boolean hasLanguage(String name, String version) {
		return languageRepository.existsByNameAndVersion(name, version);
	}

	public Language createLanguage(Language language) {
		return languageRepository.saveLanguage(language);
	}

	public List<Language> getLanguages() {
		return languageRepository.getLanguages();
	}

	public void deleteLanguage(Long languageId) {
		languageRepository.deleteLanguage(languageId);
	}

}
