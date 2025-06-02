package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.repository.LanguageRepository;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LanguageRepositoryImpl implements LanguageRepository {

	private final LanguageJpaRepository languageJpaRepository;

	@Override
	public boolean existsById(Long languageId) {
		return languageJpaRepository.existsById(languageId);
	}

	@Override
	public boolean existsByNameAndVersion(String name, String version) {
		return languageJpaRepository.existsByNameAndVersion(name, version);
	}

	@Override
	public Language saveLanguage(Language language) {
		return languageJpaRepository.save(language);
	}

	@Override
	public Optional<Language> findLanguageById(Long languageId) {
		return languageJpaRepository.findById(languageId);
	}

	@Override
	public List<Language> findLanguages() {
		return languageJpaRepository.findAll();
	}

	@Override
	public void updateLanguage(Language language, Long judge0Id) {
		language.updateJudge0Id(judge0Id);
	}

	@Override
	public void deleteLanguage(Long languageId) {
		languageJpaRepository.deleteById(languageId);
	}
}
