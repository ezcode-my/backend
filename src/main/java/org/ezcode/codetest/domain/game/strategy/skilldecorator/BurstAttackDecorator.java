package org.ezcode.codetest.domain.game.strategy.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
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
			case LEGENDARY -> 0.20;
			case UNIQUE    -> 0.15;
			case RARE      -> 0.10;
			case UNCOMMON  -> 0.07;
			case COMMON    -> 0.03;
			default        -> 0.0;
		};
		if (accBuff > 0.0) {
			attacker.applyAccuracyBuff(accBuff * attacker.getAccuracy());
			log.add("[%s] 효과로 명중률 +%.0f%% 추가 적용", skillName, accBuff);
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
