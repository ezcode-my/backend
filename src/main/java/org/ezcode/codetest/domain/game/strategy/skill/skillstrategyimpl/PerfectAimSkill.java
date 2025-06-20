package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class PerfectAimSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.PERFECT_AIM;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		attacker.consumeActionPoints();
		double multiplier = 3.0;

		attacker.applyAccuracyBuff(multiplier * attacker.getAccuracy());
		log.add("%s의 정밀 조준. 전투 종료 시까지 명중률이 %s배 상승합니다.", attacker.getName(), String.valueOf(multiplier));

		if (!rollHit(attacker, defender, log)) return true;

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double rawDamage = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double damageDealt = Math.max(rawDamage - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(rawDamage);

		log.add(isCrit
				? "%s의 치명적 정밀 조준. %s의 몸에 한 방을 꽂았습니다. %,.0f 피해."
				: "%s의 정밀 조준. %s의 몸에 한 방을 꽂았습니다. %,.0f 피해.",
			attacker.getName(), defender.getName(), damageDealt
		);

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴! %s의 행동력 1 감소 -> %d. 남은 AP %d", defender.getName(), defender.getAp(),defender.getAp());
		}

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) log.add("%s이(가) %s를 쓰러뜨렸습니다.", attacker.getName(), defender.getName());

		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean hit = RNG.nextDouble() * 100 < hitChance;

		if (!hit) {
			log.add("명중률 버프를 받고도 %s는 %s의 공격을 피해버렸습니다. 얼마나 운이 없는지 보여주는 순간입니다.", defender.getName(), attacker.getName());
		}

		return hit;
	}
}
