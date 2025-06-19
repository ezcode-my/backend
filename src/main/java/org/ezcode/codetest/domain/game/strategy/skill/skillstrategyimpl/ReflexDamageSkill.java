package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class ReflexDamageSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.REFLEX_DAMAGE;
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		if (!rollHit(attacker, defender, log)) {
			return true;
		}

		attacker.consumeActionPoints();

		double baseReflex = defender.getAtk() * 1.5;

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double rawReflex = baseReflex * (isCrit ? CRIT_MULTIPLIER : 1.0);

		boolean alive = defender.playerDamaged(rawReflex);

		if (isCrit) {
			log.add("%s의 치명적 반사 피해! 상대 공격력의 150%%(%,.1f) ×%.1f배 → %,.1f 피해를 입혔습니다.",
				attacker.getName(), baseReflex, CRIT_MULTIPLIER, Math.max(rawReflex - defender.getDef(), 0.0));
		} else {
			log.add("%s의 반사 피해! 상대 공격력의 150%%(%,.1f) 만큼 → %,.1f 피해를 입혔습니다.",
				attacker.getName(), baseReflex, Math.max(rawReflex - defender.getDef(), 0.0));
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 발생! %s의 행동력 1 감소 → %d", defender.getName(), defender.getAp());
		}

		double baseSelf = defender.getAtk() * 0.5;
		double currentHp = attacker.getHp();
		double actualSelf = Math.min(baseSelf, currentHp - 1);
		attacker.playerDamaged(actualSelf);

		log.add("반사 피해 대가로 자신은 상대 공격력 50%%(%,.1f) 중 실제 %,.1f 피해를 입었습니다.",
			baseSelf, actualSelf);

		log.add("[%s] HP: %,.1f | [%s] HP: %,.1f",
			attacker.getName(), attacker.getHp(),
			defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s은(는) 반사된 힘에 의해 완전히 제압당했습니다!", defender.getName());
			log.add("%s이(가) %s를 쓰러뜨렸습니다!",
				attacker.getName(), defender.getName());
		}

		return alive;
	}
}
