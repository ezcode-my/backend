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
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		switch (grade) {
			case LEGENDARY -> {
				if (Math.random() < 0.50) {
					defender.consumeActionPoints();
					log.add("[%s] 효과 발동. %s의 정신이 산산조각 났습니다 — AP 1 감소, 남은 AP %d", skillName, defender.getName(),
						defender.getAp());
				}
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.5);
				log.add("[%s] 효과로 %s의 명중률 50%% 감소. 머릿속 회로가 제대로 작동하지 않습니다.", skillName, defender.getName());
			}
			case UNIQUE -> {
				if (Math.random() < 0.35) {
					defender.consumeActionPoints();
					log.add("[%s] 효과 발동. %s의 멘탈이 휘청입니다 — AP 1 감소, 남은 AP %d", skillName, defender.getName(),
						defender.getAp());
				}
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.35);
				log.add("[%s] 효과로 %s의 명중률 35%% 감소. 정신이 산만해졌습니다.", skillName, defender.getName());
			}
			case RARE -> {
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.27);
				log.add("[%s] 효과 적용 — %s의 명중률 27%% 감소. 뇌가 멍해지는 중입니다.", skillName, defender.getName());
			}
			case UNCOMMON -> {
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.20);
				log.add("[%s] 효과 발동 — %s의 명중률 20%% 감소. 신경이 곤두서 있습니다.", skillName, defender.getName());
			}
			case COMMON -> {
				defender.applyAccuracyDebuff(defender.getAccuracy() * 0.15);
				log.add("[%s] 효과 — %s의 명중률 15%% 감소. 점점 정신이 흐려집니다.", skillName, defender.getName());
			}
			default -> {
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
