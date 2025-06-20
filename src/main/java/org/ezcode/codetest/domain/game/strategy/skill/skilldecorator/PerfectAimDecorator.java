package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

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
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		double accBuff = switch (grade) {
			case LEGENDARY -> 0.30;
			case UNIQUE -> 0.20;
			case RARE -> 0.15;
			case UNCOMMON -> 0.10;
			case COMMON -> 0.05;
			default -> 0.0;
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
			log.add("[%s] 효과 발동 — 명중률 +%.0f%% 추가 적용. 조준은 정확해야만 살 수 있습니다.", skillName, accBuff * 300);
		}
		if (debuff > 0.0) {
			defender.applyAtkDebuff(debuff * defender.getAtk());
			log.add("[%s] 효과 발동 — %s의 공격력 -%.0f%% 감소.", skillName, defender.getName(),
				debuff * 100);
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
