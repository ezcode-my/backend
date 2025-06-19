package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class DefencePenetrationDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public DefencePenetrationDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		double ratio = switch (grade) {
			case LEGENDARY -> 1.00;
			case UNIQUE    -> 0.70;
			case RARE      -> 0.50;
			case UNCOMMON  -> 0.30;
			case COMMON    -> 0.10;
			default        -> 0.0;
		};

		if (ratio > 0) {
			double penetrationBuff = defender.getDef() * ratio * 0.5;
			attacker.applyAtkBuff(penetrationBuff);
			log.add("[%s 효과] 방어력 관통력이 +%,.1f 더 추가됩니다.",
				skillName, defender.getName(), ratio * 50);
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
