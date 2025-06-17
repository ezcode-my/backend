package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
class BloodyMessSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() { return SkillEffect.BLOODY_MESS; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (!rollHit(attacker, defender, log)) return true;

		attacker.consumeActionPoints();

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();

		double raw = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);
		double dealt = Math.max(raw - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(raw);

		log.add(isCrit
				? "%s의 잔혹한 치명적 일격! %s에게 %,.1f 피해를 입혔습니다. %s(은)는 사지가 잘려나갔습니다."
				: "%s의 난도질! %s에게 %,.1f 피해를 입혔습니다. %s는 사지가 잘려나갔습니다.",
			attacker.getName(), defender.getName(), dealt, defender.getName()
		);

		defender.consumeActionPoints();
		log.add("%s(은)는 잘려나간 사지로 행동에 제한을 받습니다 AP 1 감소 -> %d", defender.getName(), defender.getAp());

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s이(가) %s의 온몸을 산산조각냈습니다!", attacker.getName(), defender.getName());
		}
		return alive;
	}
}
