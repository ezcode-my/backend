package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class DefencePenetrationSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.DEFENCE_PENETRATION;
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		double penetrationBuff = defender.getDef() * 0.5;
		attacker.applyAtkBuff(penetrationBuff);
		log.add("%s의 방어력 관통버프! %s의 방어력 50%%(%,.1f) 만큼 무시하고 공격합니다.",
			attacker.getName(), defender.getName(), penetrationBuff);

		if (!rollHit(attacker, defender, log)) {
			return true;
		}

		attacker.consumeActionPoints();

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double rawDamage = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);

		double dealt = Math.max(rawDamage - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(rawDamage);

		if (isCrit) {
			log.add("치명타! %s에게 %,.1f 피해를 입혔습니다.", defender.getName(), dealt);
		} else {
			log.add("%s의 공격! %s에게 %,.1f 피해를 입혔습니다.",
				attacker.getName(), defender.getName(), dealt);
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 발생! %s의 행동력 1 감소 → %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f | [%s] HP: %,.1f",
			attacker.getName(), attacker.getHp(),
			defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s의 갑옷은 산산조각 나며, 형체를 알아볼 수 없을 정도로 부서졌습니다.", defender.getName());
			log.add("%s이(가) %s를 완전히 분쇄했습니다!", attacker.getName(), defender.getName());
		}

		return alive;
	}
}