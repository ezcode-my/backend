package org.ezcode.codetest.domain.game.util;

import java.util.Map;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.character.CategoryStat;
import org.ezcode.codetest.domain.game.model.character.Stat;
import org.ezcode.codetest.domain.game.repository.CategoryStatRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatUpdateUtil {

	private final CategoryStatRepository categoryStatRepository;

	public CategoryStat save(CategoryStat categoryStat) {

		return categoryStatRepository.save(categoryStat);
	}

	public Map<Stat, Double> getStatIncreasePerProblemCategory(String categoryName) {

		CategoryStat categoryStat = categoryStatRepository.findByCategoryName(categoryName)
			.orElseThrow(() -> new GameException(GameExceptionCode.CATEGORY_STAT_NOT_FOUND));

		return categoryStat.getStats();
	}

}
