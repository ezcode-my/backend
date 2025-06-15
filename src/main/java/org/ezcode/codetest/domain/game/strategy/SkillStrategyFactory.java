package org.ezcode.codetest.domain.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.game.model.enums.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class SkillStrategyFactory {

	private final Map<SkillEffect, SkillStrategy> skillFactory;

	public SkillStrategyFactory(List<SkillStrategy> strategies) {

		skillFactory = strategies.stream()
			.collect(Collectors.toMap(SkillStrategy::getType, Function.identity()));
	}

	public SkillStrategy getStrategy(SkillEffect effect) {
		return skillFactory.get(effect);
	}

}
