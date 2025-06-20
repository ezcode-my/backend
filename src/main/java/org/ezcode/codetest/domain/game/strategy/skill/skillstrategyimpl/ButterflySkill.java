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
		log.add("%s의 나비의 춤. 방아쇠 대신 발끝이 반응합니다 — 회피율 %s배 상승.", attacker.getName(), String.valueOf(multiplier));
		boolean aliveOverall = true;

		for (int i = 1; i <= 2; i++) {
			if (!rollHit(attacker, defender, log)) {
				log.add("%s의 나비의 일격 %d타. %s을 맞출 생각이 있었는지는 의문입니다 — 명중 실패.", attacker.getName(), i, defender.getName());
				continue;
			}
			boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
			double rawDamage = attacker.getAtk() * 0.5 * (isCrit ? CRIT_MULTIPLIER : 1.0);
			double damageDealt = Math.max(rawDamage, 0.0);
			aliveOverall = defender.playerDamaged(rawDamage + defender.getDef());
			log.add(isCrit
					? "%s의 나비의 일격 %d타(치명)! 실루엣만 스친 줄 알았는데 내장이 나왔습니다 — %s에게 %,.1f 피해."
					: "%s의 나비의 일격 %d타 칼짓 하나로 %s의 신체 구조를 재구성했습니다 — %,.1f 피해.",
				attacker.getName(), i, defender.getName(), damageDealt
			);
			if (RNG.nextDouble() * 100 < attacker.getStun()) {
				defender.consumeActionPoints();
				log.add("스턴 발생. %s의 평형 감각이 날아갔습니다 — 행동력 1 감소 → %d", defender.getName(), defender.getAp());
			}

			log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

			if (!aliveOverall) {
				log.add("%s의 무도회 피날레가 %s의 삶을 마감했습니다. 커튼콜은 없습니다.", attacker.getName(), defender.getName());
				log.add("전투 결과: %s 승리", attacker.getName());
				break;
			}
		}
		return aliveOverall;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		return !(RNG.nextDouble() * 100 >= hitChance);
	}
}



