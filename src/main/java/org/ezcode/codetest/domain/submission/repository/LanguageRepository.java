package org.ezcode.codetest.domain.submission.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.submission.model.entity.Language;

public interface LanguageRepository {
	boolean existsById(Long languageId);

	boolean existsByNameAndVersion(String name, String version);

	Language saveLanguage(Language language);

	Optional<Language> findLanguageById(Long languageId);

	List<Language> findLanguages();

	void updateLanguage(Language language, Long judge0Id);

	void deleteLanguage(Long languageId);
}
