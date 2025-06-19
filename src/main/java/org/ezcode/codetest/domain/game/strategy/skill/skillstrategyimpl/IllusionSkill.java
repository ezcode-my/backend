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
		log.add("%s의 환영이 나타나 6개의 분신이 생성됩니다!", attacker.getName());

		double cloneAtkBase = attacker.getAtk() * 0.4;
		boolean alive = true;

		for (int i = 1; i <= 6; i++) {
			if (!alive) break;

			double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());

			if (RNG.nextDouble() * 100 >= hitChance) {
				log.add("분신 %d의 공격! 하지만 %s에게 빗나갔습니다.", i, defender.getName());
				continue;
			}

			boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
			double rawDamage = cloneAtkBase * (isCrit ? CRIT_MULTIPLIER : 1.0);
			double dealt = Math.max(rawDamage - defender.getDef(), 0.0);

			alive = defender.playerDamaged(rawDamage);

			if (isCrit) {
				log.add("분신 %d의 치명타! %s에게 %,.1f 피해를 입혔습니다.", i, defender.getName(), dealt);
			} else {
				log.add("분신 %d의 공격! %s에게 %,.1f 피해를 입혔습니다.", i, defender.getName(), dealt);
			}

			if (RNG.nextDouble() * 100 < attacker.getStun()) {
				defender.consumeActionPoints();
				log.add("스턴 발생! %s의 행동력 1 감소 → %d", defender.getName(), defender.getAp());
			}

			log.add("[%s] HP: %,.1f | [%s] HP: %,.1f",
				attacker.getName(), attacker.getHp(),
				defender.getName(), defender.getHp());
		}

		if (!alive) {
			log.add("%s는 환영의 분신들에게 연달아 난자당해 처참히 쓰러졌습니다!", defender.getName());
			log.add("%s이(가) %s를 완전히 제압했습니다!",
				attacker.getName(), defender.getName());
		}

		return alive;
	}
}
