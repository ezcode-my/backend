package org.ezcode.codetest.domain.game.strategy.SkillDecorator;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

public class LifeStealDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public LifeStealDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		double atkBuff = switch (grade) {
			case LEGENDARY -> 0.20;
			case UNIQUE    -> 0.15;
			case RARE      -> 0.10;
			case UNCOMMON  -> 0.05;
			case COMMON    -> 0.00;
			default        -> 0.0;
		};
		if (atkBuff > 0.0) {
			attacker.applyAtkBuff(atkBuff * attacker.getAtk());
			log.add("[%s] 효과로 공격력 +%.0f%% 추가 적용", skillName, atkBuff * 100);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
