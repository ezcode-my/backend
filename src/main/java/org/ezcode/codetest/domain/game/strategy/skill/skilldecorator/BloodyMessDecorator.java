package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

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
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		double critBuffRatio = switch (grade) {
			case LEGENDARY -> 0.35;
			case UNIQUE -> 0.15;
			case RARE -> 0.10;
			case UNCOMMON -> 0.07;
			case COMMON -> 0.04;
			default -> 0.0;
		};

		if (critBuffRatio > 0.0) {
			double critBuff = attacker.getCrit() * critBuffRatio;
			attacker.applyCritBuff(critBuff);

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 발동. 치명타 확률이 +%.1f%% 추가 증가. 이제 치명타 확률은 의사보다 빠르고, 도축장보다 정확합니다.",
					skillName, critBuffRatio * 100
				);
				case UNIQUE -> log.add(
					"[%s] 효과가 활성화됨. 치명타 확률이 +%.1f%% 추가 증가. 적의 심장이 '나도 끝났구나'라고 속삭입니다.",
					skillName, critBuffRatio * 100
				);
				case RARE -> log.add(
					"[%s] 효과 적용. 치명타 확률이 +%.1f%% 추가 증가. 오늘도 누군가 갈갈이 찢길 준비를 마쳤습니다.",
					skillName, critBuffRatio * 100
				);
				case UNCOMMON -> log.add(
					"[%s] 발동. 치명타 확률이 +%.1f%% 추가 증가. 회복약이 아니라 응급 수술 키트가 필요하겠네요.",
					skillName, critBuffRatio * 100
				);
				case COMMON -> log.add(
					"[%s] 효과 발동. 치명타 확률이 +%.1f%% 추가 증가.",
					skillName, critBuffRatio * 100
				);
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
