package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class BloodyMessSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.BLOODY_MESS;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {

		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		if (RNG.nextDouble() * 100 >= hitChance) {
			log.add("%s의 공격. 하지만 %s는 요상하게 몸을 비틀며 튀어나온 내장을 간신히 지켰습니다.", attacker.getName(), defender.getName());
			return false;
		}
		return true;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		attacker.consumeActionPoints();

		if (!rollHit(attacker, defender, log))
			return true;


		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();

		double raw = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(raw);

		if (isCrit) {
			log.add("%s의 외침과 함께 치명타. %s의 장기가 공중에서 회전하며 굿바이 인사를 남깁니다. 피해: %,.1f", attacker.getName(), defender.getName(),
				dealt);
		} else {
			log.add("%s의 난도질! %s는 제자리에 서 있었지만, 사지는 그렇지 않았습니다. 피해: %,.1f", attacker.getName(), defender.getName(),
				dealt);
		}

		defender.consumeActionPoints();

		log.add("%s는 사지를 잃고 비틀거립니다. AP 1 감소 → %d", defender.getName(), defender.getAp());
		log.add("[%s] HP: %,.1f / [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(),
			defender.getHp());

		if (!alive) {
			log.add("%s의 공격은 예술이었습니다. %s는 이제 전시용 고깃덩어리입니다.", attacker.getName(), defender.getName());
			log.add("※ 전투 종료: %s의 압도적인 승리!", attacker.getName());
		}

		return alive;
	}
}
