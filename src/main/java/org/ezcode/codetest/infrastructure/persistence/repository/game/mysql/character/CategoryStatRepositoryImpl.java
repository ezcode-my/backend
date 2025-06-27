package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.CategoryStat;
import org.ezcode.codetest.domain.game.repository.CategoryStatRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CategoryStatRepositoryImpl implements CategoryStatRepository {

	private final CategoryStatJpaRepository statCategoryJRepository;

	@Override
	public CategoryStat save(CategoryStat categoryStat) {

		return statCategoryJRepository.save(categoryStat);
	}

	@Override
	public Optional<CategoryStat> findByCategoryId(Long categoryId) {

		return statCategoryJRepository.findByCategoryId(categoryId);
	}

	@Override
	@Cacheable(value = "categoryStat", key = "#categoryName")
	public Optional<CategoryStat> findByCategoryName(String categoryName) {

		return statCategoryJRepository.findByCategoryKorName(categoryName);
	}

}
