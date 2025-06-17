package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;


import java.util.Random;

import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;
import org.springframework.stereotype.Component;

@Component
public class NoSkill implements SkillStrategy {

	private static final Random RNG = new Random();
	private static final double BASE_HIT_RATE = 50.0;
	private static final double CRIT_MULTIPLIER = 1.5;

	@Override
	public SkillEffect getType() {
		return SkillEffect.NO_SKILL;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType attackerWeapon) {

		attacker.consumeActionPoints();

		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean isHit = RNG.nextDouble() * 100 < hitChance;
		if (!isHit) {
			log.add("%s의 공격! 하지만 %s에게 빗나갔습니다.", attacker.getName(), defender.getName());
			return true;
		}

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();

		double rawDamage = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double damageDealt = Math.max(rawDamage - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(rawDamage);

		if (isCrit) {
			log.add("%s의 치명타! %s에게 %,.1f의 피해를 입혔습니다.", attacker.getName(), defender.getName(), damageDealt);
		} else {
			log.add("%s의 일반 공격! %s에게 %,.1f의 피해를 입혔습니다.", attacker.getName(), defender.getName(), damageDealt);
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			boolean hasAp = defender.consumeActionPoints();
			log.add("스턴 효과! %s의 행동력이 1 감소했습니다. 현재 행동력: %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s이(가) %s를 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
		}

		return alive;
	}
}
