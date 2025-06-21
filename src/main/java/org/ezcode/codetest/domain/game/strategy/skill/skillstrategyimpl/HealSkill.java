package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HealSkill extends AbstractSkill {

	private final NoSkill fallback;

	@Override
	public SkillEffect getType() { return SkillEffect.HEAL; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (weaponType != WeaponType.SYRINGE) {
			return fallback.useSkill(attacker, defender, log, weaponType);
		}
		double amount = attacker.getAtk() * 0.5;
		attacker.restoreHp(amount);

		log.add("%s의 피가 회복되었습니다 — %,.1f HP 추가 회복.", attacker.getName(), amount);
		log.add("이 정도면 살아날 가능성 조금 상승.");
		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		return fallback.useSkill(attacker, defender, log, weaponType);
	}
}
