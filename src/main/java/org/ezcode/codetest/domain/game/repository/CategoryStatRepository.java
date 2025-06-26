package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.CategoryStat;

public interface CategoryStatRepository {

	Optional<CategoryStat> findByCategoryId(Long categoryId);

	Optional<CategoryStat> findByCategoryName(String name);

	CategoryStat save(CategoryStat categoryStat);
}
