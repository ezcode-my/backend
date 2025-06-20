package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import java.util.Random;

import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;
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
			log.add("%s의 공격이 빗나갔습니다. %s(은)는 멀쩡히 웃고 있습니다.", attacker.getName(), defender.getName());
			return true;
		}

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();

		double rawDamage = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double damageDealt = Math.max(rawDamage - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(rawDamage);

		if (isCrit) {
			log.add("%s의 치명타. %s에게 깊은 상처를 남겼습니다 — %,.1f 피해.", attacker.getName(), defender.getName(), damageDealt);
		} else {
			log.add("%s의 공격. %s(은)는 한 대 맞았습니다 — %,.1f 피해.", attacker.getName(), defender.getName(), damageDealt);
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴. %s의 행동력이 1 줄었습니다. 아직 정신은 멀쩡합니다. 남은 AP %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] 남은 체력: %,.1f | [%s] 남은 체력: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s님이 %s를 눕혔습니다.", attacker.getName(), defender.getName());
		}

		return alive;
	}
}
