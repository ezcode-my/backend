package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class LifeStealSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.LIFE_STEAL;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		attacker.consumeActionPoints();

		if (!rollHit(attacker, defender, log))
			return true;

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double raw = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(raw);
		double heal = dealt * 0.3;
		attacker.restoreHp(heal);

		if (isCrit) {
			log.add("%s가 %s의 피를 슬쩍했습니다. %,.1f 피해 주고 %,.1f 회복.",
				attacker.getName(), defender.getName(), dealt, heal);
		} else {
			log.add("%s의 생명 흡수. %s에게 %,.1f 피해를 주고 %,.1f 회복했습니다.",
				attacker.getName(), defender.getName(), dealt, heal);
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴! %s(이)가 잠시 정신을 잃었습니다 — 행동력 1 감소 → 남은 AP %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] 남은 체력: %,.1f | [%s] 남은 체력: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s가 %s를 눕혔습니다.", attacker.getName(), defender.getName());
		}

		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean hit = !(RNG.nextDouble() * 100 >= hitChance);
		if (!hit) {
			log.add("%s가 공격을 시도했지만 빗나갔습니다. 흡혈할 수있어도 못 맞추면 무슨 의미죠? %s는 피 한 방울도 흘리지 않고 교묘하게 피했습니다.",
				attacker.getName(), defender.getName());
		}
		return hit;
	}
}
