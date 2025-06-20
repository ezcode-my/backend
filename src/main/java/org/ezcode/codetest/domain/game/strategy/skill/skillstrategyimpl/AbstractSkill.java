package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import java.util.Random;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;


public abstract class AbstractSkill implements SkillStrategy {

	protected static final Random RNG = new Random();
	protected static final double BASE_HIT_RATE = 50.0;
	protected static final double CRIT_MULTIPLIER = 1.5;

	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {

		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		if (RNG.nextDouble() * 100 >= hitChance) {
			log.add("%s의 공격. 하지만 %s에게 빗나갔습니다.", attacker.getName(), defender.getName());
			attacker.consumeActionPoints();
			return false;
		}
		return true;
	}
}
