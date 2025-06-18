package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class ButterflySkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.BUTTERFLY;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		attacker.consumeActionPoints();

		double originalEvasion = attacker.getEvasion();
		double multiplier = 2.0;

		attacker.applyEvasionBuff(originalEvasion * multiplier);
		log.add("%s의 나비의 춤! 전투 종료 시까지 회피율이 %s배 상승합니다.", attacker.getName(), String.valueOf(multiplier));
		boolean aliveOverall = true;

		for (int i = 1; i <= 2; i++) {
			if (!rollHit(attacker, defender, log)) {
				log.add("%s의 나비의 일격 %d타! 빗나갔습니다.", attacker.getName(), i);
				continue;
			}
			boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
			double rawDamage = attacker.getAtk() * 0.5 * (isCrit ? CRIT_MULTIPLIER : 1.0);
			double damageDealt = Math.max(rawDamage - defender.getDef(), 0.0);
			aliveOverall = defender.playerDamaged(rawDamage);
			log.add(isCrit
					? "%s의 나비의 일격 %d타(치명)! %s에게 %,.1f 피해를 입혔습니다."
					: "%s의 나비의 일격 %d타! %s에게 %,.1f 피해를 입혔습니다.",
				attacker.getName(), i, defender.getName(), damageDealt
			);
			if (RNG.nextDouble() * 100 < attacker.getStun()) {
				defender.consumeActionPoints();
				log.add("스턴! %s의 행동력 1 감소 -> %d", defender.getName(), defender.getAp());
			}

			log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

			if (!aliveOverall) {
				log.add("%s이(가) %s를 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
				break;
			}
		}
		return aliveOverall;
	}
}

