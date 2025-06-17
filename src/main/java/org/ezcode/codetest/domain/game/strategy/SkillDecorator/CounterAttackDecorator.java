package org.ezcode.codetest.domain.game.strategy.SkillDecorator;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

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
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		double buff = switch (grade) {
			case LEGENDARY -> 0.10;
			case UNIQUE    -> 0.08;
			case RARE      -> 0.06;
			case UNCOMMON  -> 0.04;
			case COMMON    -> 0.02;
			default        -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyCritBuff(buff * attacker.getCrit());
			log.add("[%s] 효과로 치명타 확률 +%.0f%% 추가 적용", skillName, buff * 100);

			attacker.applyStunBuff(buff * attacker.getStun());
			log.add("[%s] 효과로 스턴 확률 +%.0f%% 추가 적용", skillName, buff * 100);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
