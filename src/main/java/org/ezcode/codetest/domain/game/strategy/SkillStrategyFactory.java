package org.ezcode.codetest.domain.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;
import org.springframework.stereotype.Component;

@Component
public class SkillStrategyFactory {

	private final Map<SkillEffect, SkillStrategy> skillFactory;

	public SkillStrategyFactory(List<SkillStrategy> strategies) {

		skillFactory = strategies.stream()
			.collect(Collectors.toMap(SkillStrategy::getType, Function.identity(), (first, second) -> first));
	}

	private SkillStrategy getStrategy(SkillEffect effect) {
		return skillFactory.get(effect);
	}

	public List<SkillStrategy> orderedStrategies(List<GameCharacterSkill> equipped) {

		Map<SkillSlotType, GameCharacterSkill> map = equipped.stream()
			.collect(Collectors.toMap(
				GameCharacterSkill::getSlotType,
				Function.identity()
			));

		return Stream.of(
				SkillSlotType.SLOT_1,
				SkillSlotType.SLOT_2,
				SkillSlotType.SLOT_3
			)
			.map(slot -> {
				GameCharacterSkill cs = map.get(slot);
				SkillEffect effect = (cs != null)
					? cs.getSkill().getSkillEffect()
					: SkillEffect.NO_SKILL;
				return getStrategy(effect);
			})
			.toList();
	}
}
