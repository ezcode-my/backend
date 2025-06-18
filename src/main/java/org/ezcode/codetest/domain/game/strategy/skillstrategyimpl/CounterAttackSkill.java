package org.ezcode.codetest.domain.game.strategy.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class CounterAttackSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() { return SkillEffect.COUNTER_ATTACK; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (!rollHit(attacker, defender, log)) return true;
		attacker.consumeActionPoints();
		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double raw = attacker.getAtk() + defender.getAtk() * 0.5;
		raw *= isCrit ? CRIT_MULTIPLIER : 1.0;
		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(raw);
		log.add(isCrit
				? "%s의 치명적 카운터 반격! %s의 공격을 더해 %,.1f 피해를 입혔습니다."
				: "%s의 카운터 반격! %s의 공격을 더해 %,.1f 피해를 입혔습니다.",
			attacker.getName(), defender.getName(), dealt
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
