package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class ReflexDamageDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public ReflexDamageDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		double buffRatio = switch (grade) {
			case LEGENDARY -> 0.10;
			case UNIQUE    -> 0.08;
			case RARE      -> 0.06;
			case UNCOMMON  -> 0.04;
			case COMMON    -> 0.02;
			default        -> 0.0;
		};

		if (buffRatio > 0) {

			double accBuff = attacker.getAccuracy() * buffRatio;
			double critBuff = attacker.getCrit()      * buffRatio;

			attacker.applyAccuracyBuff(accBuff);
			log.add("[%s 효과] 명중 +%.0f%% (+%,.1f)", skillName, buffRatio * 100, accBuff);

			attacker.applyCritBuff(critBuff);
			log.add("[%s 효과] 치명타 +%.0f%% (+%,.1f)", skillName, buffRatio * 100, critBuff);
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
