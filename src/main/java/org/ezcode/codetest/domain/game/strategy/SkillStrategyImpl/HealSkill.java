package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
class HealSkill extends AbstractSkill {

	private final NoSkill fallback = new NoSkill();

	@Override
	public SkillEffect getType() { return SkillEffect.HEAL; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (weaponType != WeaponType.MAGIC_BOOK) {
			return fallback.useSkill(attacker, defender, log, weaponType);
		}
		attacker.consumeActionPoints();
		double amount = attacker.getAtk() * 0.5;
		attacker.restoreHp(amount);
		log.add("%s의 회복! %,.1f 회복.", attacker.getName(), amount);

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		return fallback.useSkill(attacker, defender, log, weaponType);
	}
}
