package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class HealDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public HealDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		if (weaponType != WeaponType.MAGIC_BOOK) {
			log.add("%s: 회복 스킬 실패. 마법서가 아닙니다.", attacker.getName());
			return delegate.useSkill(attacker, defender, log, weaponType);
		}

		double healRatio = switch (grade) {
			case LEGENDARY -> 0.50;
			case UNIQUE -> 0.40;
			case RARE -> 0.30;
			case UNCOMMON -> 0.20;
			case COMMON -> 0.10;
			default -> 0.0;
		};

		double healAmount = attacker.getAtk() * healRatio;
		if (healAmount > 0.0) {
			attacker.restoreHp(healAmount);
			log.add("[%s] 효과로 HP %,.1f 추가 회복 (공격력의 %.0f%%)", skillName, healAmount, healRatio * 100);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
