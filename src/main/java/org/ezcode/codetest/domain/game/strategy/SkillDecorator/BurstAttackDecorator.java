package org.ezcode.codetest.domain.game.strategy.SkillDecorator;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

public class BurstAttackDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public BurstAttackDecorator(SkillStrategy delegate, Grade grade, String skillName) {
		this.delegate = delegate;
		this.grade = grade;
		this.skillName = skillName;
	}

	@Override
	public SkillEffect getType() { return delegate.getType(); }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (weaponType != WeaponType.SHOT_GUN) {
			log.add("%s: 버스트 어택 실패. 산탄총이 아닙니다.", attacker.getName());
			return true;
		}

		double accBuff = switch (grade) {
			case LEGENDARY -> 20.0;
			case UNIQUE    -> 15.0;
			case RARE      -> 10.0;
			case UNCOMMON  -> 7.0;
			case COMMON    -> 3.0;
			default        -> 0.0;
		};
		if (accBuff > 0.0) {
			attacker.applyAccuracyBuff(accBuff * attacker.getAccuracy());
			log.add("[%s] 효과로 명중률 +%.0f%% 추가 적용", skillName, accBuff);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
