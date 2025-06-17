package org.ezcode.codetest.domain.game.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.BloodyMessDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.BurstAttackDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.ButterflyDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.CounterAttackDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.DefenceDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.HealDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.InstantKillDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.LifeStealDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.MentalDestroyDecorator;
import org.ezcode.codetest.domain.game.strategy.SkillDecorator.PerfectAimDecorator;
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

	private SkillStrategy getStrategyDecorate(SkillEffect effect, Grade grade, String skillName, SkillStrategy strategy) {
		return switch (effect) {
			case LIFE_STEAL -> new LifeStealDecorator(strategy, grade, skillName);
			case COUNTER_ATTACK -> new CounterAttackDecorator(strategy, grade, skillName);
			//case REFLEX_DAMAGE -> new ReflexDamageDecorator(strategy, grade, skillName);
			case HEAL -> new HealDecorator(strategy, grade, skillName);
			case BURST_ATTACK -> new BurstAttackDecorator(strategy, grade, skillName);
			case INSTANT_KILL -> new InstantKillDecorator(strategy, grade, skillName);
			case BLOODY_MESS -> new BloodyMessDecorator(strategy, grade, skillName);
			case DEFENCE -> new DefenceDecorator(strategy, grade, skillName);
			//case ILLUSION -> new IllusionDecorator(strategy, grade, skillName);
			case PERFECT_AIM -> new PerfectAimDecorator(strategy, grade, skillName);
			case BUTTERFLY -> new ButterflyDecorator(strategy, grade, skillName);
			case MENTAL_DESTROY -> new MentalDestroyDecorator(strategy, grade, skillName);
			case NO_SKILL -> strategy;
		};
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
				String skillName = (cs != null) ? cs.getSkill().getName() : null;
				Grade grade = (cs != null) ? cs.getSkill().getGrade() : null;
				SkillEffect effect = (cs != null)
					? cs.getSkill().getSkillEffect()
					: SkillEffect.NO_SKILL;
				return getStrategyDecorate(effect, grade, skillName, getStrategy(effect));
			})
			.toList();
	}
}
