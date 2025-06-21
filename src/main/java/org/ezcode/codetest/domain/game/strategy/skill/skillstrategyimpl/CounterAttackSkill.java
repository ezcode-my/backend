package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class CounterAttackSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.COUNTER_ATTACK;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		attacker.consumeActionPoints();

		if (!rollHit(attacker, defender, log)) return true;

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();

		double raw = attacker.getAtk() + defender.getAtk() * 0.5;
		raw *= isCrit ? CRIT_MULTIPLIER : 1.0;

		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(raw);

		log.add(isCrit
				? "%s의 치명적인 일격 %s의 힘을 빌려 무자비하게 %,.1f를 입혔습니다."
				: "%s의 반격 %s의 공격력을 담아 %,.1f 피해를 주었습니다.",
			attacker.getName(), defender.getName(), dealt
		);

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 %s가 균형을 잃고 행동력이 1 줄었습니다 → 남은 AP %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] 체력 상태: %,.1f, [%s] 체력 상태: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s가 %s를 무참히 처단했습니다. 이 게임에 두 번째 기회는 없습니다.", attacker.getName(), defender.getName());
		}

		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean hit = RNG.nextDouble() * 100 < hitChance;
		if (!hit) {
			log.add("%s의 반격이 빗나갔습니다. %s는 재빠르게 피해 냈습니다.", attacker.getName(), defender.getName());
		}
		return hit;
	}
}
