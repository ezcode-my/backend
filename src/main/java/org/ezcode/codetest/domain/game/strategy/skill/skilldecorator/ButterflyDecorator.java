package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class ButterflyDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public ButterflyDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		double buff = switch (grade) {
			case LEGENDARY -> 0.20;
			case UNIQUE -> 0.12;
			case RARE -> 0.06;
			case UNCOMMON -> 0.04;
			case COMMON -> 0.02;
			default -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyAtkBuff(buff * attacker.getAtk());
			attacker.applyEvasionBuff(buff * attacker.getEvasion());

			switch (grade) {
				case LEGENDARY -> {
					log.add("[%s] 효과 발동 — 공격력 +%.0f%%, 회피율 +%.0f%% 추가 증가.", skillName, buff * 100, buff * 100);
					log.add("※ 적이 화려함에 모욕감을 느꼈습니다.");
				}
				case UNIQUE, RARE, UNCOMMON, COMMON -> {
					log.add("[%s] 효과 발동 — 공격력 +%.0f%%, 회피율 +%.0f%% 추가 증가.", skillName, buff * 100, buff * 100);
				}
				default -> {
				}
			}
		}
		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
