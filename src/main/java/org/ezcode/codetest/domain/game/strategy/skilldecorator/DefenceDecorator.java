package org.ezcode.codetest.domain.game.strategy.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

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
			case LEGENDARY -> 0.35;
			case UNIQUE -> 0.28;
			case RARE -> 0.20;
			case UNCOMMON -> 0.12;
			case COMMON -> 0.06;
			default -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyDefBuff(buff * attacker.getDef());
			log.add("[%s] 효과로 방어력 +%.0f%% 추가 적용", skillName, buff * 100);
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
