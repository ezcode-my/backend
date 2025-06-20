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

		if (weaponType != WeaponType.SYRINGE) {
			log.add("%s: 이거 주사기가 아닌데요? 걍 주먹으로 두드리겠음.", attacker.getName());
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

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 효과 발동. HP %,.1f 추가 회복 (공격력의 %.0f%%). 효과가 너무 좋아서 팔이 하나 더 돋았습니다.",
					skillName, healAmount, healRatio * 100
				);
				case UNIQUE -> log.add(
					"[%s] 효과 발동. HP %,.1f 추가 회복 (공격력의 %.0f%%). 재생 속도가 비정상적으로 빨라졌습니다.",
					skillName, healAmount, healRatio * 100
				);
				case RARE -> log.add(
					"[%s] 효과 발동. HP %,.1f 추가 회복 (공격력의 %.0f%%). 근육과 신경이 순간 재조직 됩니다. 과용 금지.",
					skillName, healAmount, healRatio * 100
				);
				case UNCOMMON -> log.add(
					"[%s] 효과 발동. HP %,.1f 추가 회복 (공격력의 %.0f%%). 반복 사용시 내성 주의.",
					skillName, healAmount, healRatio * 100
				);
				case COMMON -> log.add(
					"[%s] 효과 발동. HP %,.1f 추가 회복 (공격력의 %.0f%%).",
					skillName, healAmount, healRatio * 100
				);
			}
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
