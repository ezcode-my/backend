package org.ezcode.codetest.domain.language.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Language;

public interface LanguageRepository {
	boolean existsById(Long languageId);

	boolean existsByNameAndVersion(String name, String version);

	Language saveLanguage(Language language);

	List<Language> getLanguages();

	void deleteLanguage(Long languageId);
}
