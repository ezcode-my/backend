package org.ezcode.codetest.domain.game.strategy.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

public class PerfectAimDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public PerfectAimDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() { return delegate.getType(); }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		double accBuff = switch (grade) {
			case LEGENDARY -> 0.10;
			case UNIQUE    -> 0.07;
			case RARE      -> 0.05;
			case UNCOMMON  -> 0.03;
			case COMMON    -> 0.01;
			default        -> 0.0;
		};

		double debuff = 0.0;
		if (grade == Grade.LEGENDARY) {
			debuff = 0.15;
		} else if (grade == Grade.UNIQUE) {
			debuff = 0.10;
		} else if (grade == Grade.RARE) {
			debuff = 0.05;
		}

		if (accBuff > 0.0) {
			attacker.applyAccuracyBuff(accBuff * attacker.getAccuracy());
			log.add("[%s] 효과로 명중률 +%.0f%% 추가 적용", skillName, accBuff * 100);
		}
		if (debuff > 0.0) {
			defender.applyAtkDebuff(debuff * defender.getAtk());
			log.add("[%s] 효과로 상대 공격력 -%.0f%% 추가 적용", skillName, debuff * 100);
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
