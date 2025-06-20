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
			case LEGENDARY -> 0.25;
			case UNIQUE -> 0.15;
			case RARE -> 0.06;
			case UNCOMMON -> 0.04;
			case COMMON -> 0.02;
			default -> 0.0;
		};

		if (buffRatio > 0) {
			double accBuff = attacker.getAccuracy() * buffRatio;
			double critBuff = attacker.getCrit() * buffRatio;

			attacker.applyAccuracyBuff(accBuff);
			attacker.applyCritBuff(critBuff);

			log.add("[%s] 효과 발동 — 명중률 +%.0f%%, 치명타 확률 +%.0f%% 동시에 증가. 상대 피를 빼앗는 대신, %s의 몸도 조금씩 부서집니다.", skillName,
				buffRatio * 100, buffRatio * 100, attacker.getName());
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
