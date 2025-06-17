package org.ezcode.codetest.domain.game.strategy.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

public class BloodyMessDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public BloodyMessDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() { return delegate.getType(); }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		double critBuff = switch (grade) {
			case LEGENDARY -> 0.20;
			case UNIQUE    -> 0.15;
			case RARE      -> 0.10;
			case UNCOMMON  -> 0.07;
			case COMMON    -> 0.04;
			default        -> 0.0;
		};
		if (critBuff > 0.0) {
			attacker.applyCritBuff(critBuff * attacker.getCrit());
			log.add("[%s] 효과로 치명타 확률 +%.0f%% 추가 적용", skillName, critBuff * 100);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
