package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class LifeStealSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() { return SkillEffect.LIFE_STEAL; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (!rollHit(attacker, defender, log)) return true;
		attacker.consumeActionPoints();

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double raw = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(dealt);
		double heal = dealt * 0.3;
		attacker.restoreHp(heal);

		log.add(isCrit
				? "%s의 치명적 생명 흡수! %s에게 %,.1f 피해 후 %,.1f 회복."
				: "%s의 생명 흡수! %s에게 %,.1f 피해 후 %,.1f 회복.",
			attacker.getName(), defender.getName(), dealt, heal
		);
		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴! %s의 행동력 1 감소 -> %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) log.add("%s이(가) %s를 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
		return alive;
	}
}
