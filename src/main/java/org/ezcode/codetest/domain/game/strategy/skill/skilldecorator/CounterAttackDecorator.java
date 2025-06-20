package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class CounterAttackDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public CounterAttackDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		double buff = switch (grade) {
			case LEGENDARY -> 0.20;
			case UNIQUE -> 0.10;
			case RARE -> 0.06;
			case UNCOMMON -> 0.04;
			case COMMON -> 0.02;
			default -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyCritBuff(buff * attacker.getCrit());
			attacker.applyStunBuff(buff * attacker.getStun());

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 발동. 치명타 +%.1f%%, 스턴 +%.1f%% 증가합니다. 적은 두 번 맞고 한 번 웃습니다. 웃는 쪽은 아닙니다.",
					skillName, buff * 100, buff * 100
				);
				case UNIQUE -> log.add(
					"[%s] 효과 발동. 치명타 +%.1f%%, 스턴 +%.1f%% 증가합니다. 반격은 인사고, 부작용은 사망입니다.",
					skillName, buff * 100, buff * 100
				);
				case RARE -> log.add(
					"[%s] 효과 적용. 치명타 +%.1f%%, 스턴 +%.1f%% 증가합니다. 운 좋으면 아픕니다. 운 나쁘면 끝입니다.",
					skillName, buff * 100, buff * 100
				);
				case UNCOMMON -> log.add(
					"[%s] 발동. 치명타 +%.1f%%, 스턴 +%.1f%% 증가합니다. 병원비는 따로 청구되지 않습니다.",
					skillName, buff * 100, buff * 100
				);
				case COMMON -> log.add(
					"[%s] 효과 발동. 치명타 +%.1f%%, 스턴 +%.1f%% 증가합니다.",
					skillName, buff * 100, buff * 100
				);
			}
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
