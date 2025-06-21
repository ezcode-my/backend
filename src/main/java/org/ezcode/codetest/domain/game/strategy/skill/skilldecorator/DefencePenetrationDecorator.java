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
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		double buff = switch (grade) {
			case LEGENDARY -> 0.50;
			case UNIQUE -> 0.20;
			case RARE -> 0.15;
			case UNCOMMON -> 0.10;
			case COMMON -> 0.05;
			default -> 0.0;
		};

		if (buff > 0.0) {
			attacker.applyAtkBuff(buff * defender.getDef());

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 발동 — 방어력 관통력 +%.0f%% 추가 상승. 방어구는 허울뿐, %s의 공격은 철갑을 꿰뚫습니다. 맞으면 바로 병원행.",
					skillName, buff * 100, attacker.getName()
				);
				case UNIQUE -> log.add(
					"[%s] 효과 작동 — 방어력 관통력 +%.0f%% 추가 증가. 강철 방패도 별수 없네요. %s(은)는 한숨을 쉽니다.",
					skillName, buff * 100, defender.getName()
				);
				case RARE -> log.add(
					"[%s] 활성화 — 방어력 관통력 +%.0f%% 추가 증가. 쇠붙이를 무너뜨리는 손길, 기억에 오래 남습니다. 상대도 피곤해집니다.",
					skillName, buff * 100
				);
				case UNCOMMON -> log.add(
					"[%s] 발동 — 방어력 관통력 +%.0f%% 추가 증가. 방패가 뚫리는 소리는 쓸데없는 소음일 뿐입니다.",
					skillName, buff * 100
				);
				case COMMON -> log.add(
					"[%s] 효과 적용 — 방어력 관통력 +%.0f%% 증가. 얇은 철판 긁는 정도지만, 그래도 상처는 남습니다.",
					skillName, buff * 100
				);
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
