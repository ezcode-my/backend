package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class MentalDestroyDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public MentalDestroyDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		switch (grade) {
			case LEGENDARY -> {
				if (Math.random() < 0.35) {
					log.add("[%s] 효과로 정신 붕괴! %s의 행동력 %d 감소", skillName, defender.getName(), defender.consumeActionPoints());
				}
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.3);
				log.add("[%s] 효과로 %s의 명중률 30%% 감소 디버프", skillName, defender.getName());
			}
			case UNIQUE -> {
				if (Math.random() < 0.25) {
					log.add("[%s] 효과로 정신 붕괴! %s의 행동력 %d 감소", skillName, defender.getName(), defender.consumeActionPoints());
				}
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.2);
				log.add("[%s] 효과로 %s의 명중률 20%% 감소 디버프", skillName, defender.getName());
			}
			default -> {
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.2);
				log.add("[%s] 효과로 %s의 명중률 20%% 감소 디버프", skillName, defender.getName());
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
