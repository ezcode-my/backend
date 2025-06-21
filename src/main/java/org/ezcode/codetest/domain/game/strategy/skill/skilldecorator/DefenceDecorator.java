package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class DefenceDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public DefenceDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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
			case LEGENDARY -> 0.50;
			case UNIQUE -> 0.25;
			case RARE -> 0.15;
			case UNCOMMON -> 0.10;
			case COMMON -> 0.05;
			default -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyDefBuff(buff * attacker.getDef());

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 효과 작동 — 방어력 +%.0f%% 추가 상승. 머리는 텅 비었지만, 방패는 철통입니다. 맞으면 아프니 조심하세요.",
					skillName, buff * 200
				);
				case UNIQUE -> log.add(
					"[%s] 발동 — 방어력 +%.0f%% 추가 증가. 생각은 없지만, 몸은 강철 같아졌습니다. 적들은 슬퍼합니다.",
					skillName, buff * 200
				);
				case RARE -> log.add(
					"[%s] 활성화 — 방어력 +%.0f%% 추가 상승. 머리는 멍청해도 몸은 돌덩이처럼 단단해졌습니다. 조심히 맞으세요.",
					skillName, buff * 200
				);
				case UNCOMMON -> log.add(
					"[%s] 효과 적용 — 방어력 +%.0f%% 추가 증가. 뇌는 느리지만, 몸은 방어하는 데 바쁩니다. 꽤 쓸 만합니다.",
					skillName, buff * 200
				);
				case COMMON -> log.add(
					"[%s] 발동 — 방어력 +%.0f%% 추가 상승. 기본은 합니다. 멍청해도 할 수 있는 방어법입니다.",
					skillName, buff * 200
				);
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
