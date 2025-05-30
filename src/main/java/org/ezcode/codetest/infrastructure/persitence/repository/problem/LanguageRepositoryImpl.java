package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.language.repository.LanguageRepository;
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
	public List<Language> getLanguages() {
		return languageJpaRepository.findAll();
	}

	@Override
	public void deleteLanguage(Long languageId) {
		languageJpaRepository.deleteById(languageId);
	}
}
