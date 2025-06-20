package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class IllusionSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.ILLUSION;
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		attacker.consumeActionPoints();

		log.add("%s의 환영이 본격적으로 사냥을 시작합니다.", attacker.getName());

		double cloneAtkBase = attacker.getAtk() * 0.4;
		boolean alive = true;

		for (int i = 1; i <= 6; i++) {
			if (!alive)
				break;

			double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());

			if (RNG.nextDouble() * 100 >= hitChance) {
				log.add("분신 %d의 칼날이 %s를 겨누었지만, 허공만 스쳤습니다. 운이 좋았습니다.", i, defender.getName());
				continue;
			}

			boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
			double rawDamage = cloneAtkBase * (isCrit ? CRIT_MULTIPLIER : 1.0);
			double dealt = Math.max(rawDamage - defender.getDef(), 0.0);

			alive = defender.playerDamaged(rawDamage);

			if (isCrit) {
				log.add("분신 %d의 치명타. %s의 몸에 작은 구멍이 하나 더 생겼습니다 — %,.1f 피해.", i, defender.getName(), dealt);
			} else {
				log.add("분신 %d의 공격. %s(은)는 뭔가 맞긴 했는데, 어디가 아픈진 모르겠습니다. — %,.1f 피해.", i, defender.getName(), dealt);
			}

			if (RNG.nextDouble() * 100 < attacker.getStun()) {
				defender.consumeActionPoints();
				log.add("스턴! %s의 행동력이 잠시 딸려갑니다 — AP 1 감소 → 남은 AP %d", defender.getName(), defender.getAp());
			}

			log.add("[%s] 체력 현황: %,.1f | [%s] 체력 현황: %,.1f",
				attacker.getName(), attacker.getHp(),
				defender.getName(), defender.getHp());
		}

		if (!alive) {
			log.add("%s는 6개의 허깨비에게 맞아 이리저리 튕기다 결국 무너졌습니다.", defender.getName());
			log.add("%s이(가) %s를 끝장냈습니다.", attacker.getName(), defender.getName());
		}

		return alive;
	}
}
