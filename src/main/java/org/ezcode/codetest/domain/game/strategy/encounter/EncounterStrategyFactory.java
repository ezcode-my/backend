package org.ezcode.codetest.domain.game.strategy.encounter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.springframework.stereotype.Component;

@Component
public class EncounterStrategyFactory {

	private final Map<RandomEncounterEffect, EncounterStrategy> encounterFactory;

	public EncounterStrategyFactory(List<EncounterStrategy> strategies) {

		encounterFactory = strategies.stream()
			.collect(Collectors.toMap(EncounterStrategy::getEffect, Function.identity(), (first, second) -> first));
	}

	public EncounterStrategy getStrategy(RandomEncounterEffect effect) {
		return encounterFactory.get(effect);
	}
}
