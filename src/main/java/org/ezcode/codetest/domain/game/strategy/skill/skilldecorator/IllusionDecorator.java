package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class IllusionDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public IllusionDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		if (weaponType != WeaponType.SHORT_SWORD && weaponType != WeaponType.PISTOL) {
			attacker.consumeActionPoints();
			log.add("%s : 환영 분신술 실패. 무기 좀 골라서 가져오세요, 이런 거대한 무기로는 정신 산만해집니다. 제대로 된 쬐끄만 무기로 다시 와요.", attacker.getName());
			return true;
		}

		double buffRatio = switch (grade) {
			case LEGENDARY -> 0.30;
			case UNIQUE -> 0.24;
			case RARE -> 0.18;
			case UNCOMMON -> 0.12;
			case COMMON -> 0.06;
			default -> 0.0;
		};

		if (buffRatio > 0) {
			double accBuff = attacker.getAccuracy() * buffRatio;
			attacker.applyAccuracyBuff(accBuff);

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s 효과] 발동 — 명중률 +%.0f%% 증가. %s가 6개의 환영을 불러냈습니다. 진짜가 누군지 알아내려 애쓰세요. 실패하면 정신줄 놓습니다.",
					skillName, buffRatio * 100, attacker.getName()
				);
				case UNIQUE -> log.add(
					"[%s 효과] 활성화 — 명중률 +%.0f%% 증가. %s의 분신들이 나타났습니다. 상대가 멘붕하기 전까지는 싸움이 시작되지 않습니다.",
					skillName, buffRatio * 100, attacker.getName()
				);
				case RARE -> log.add(
					"[%s 효과] 작동 — 명중률 +%.0f%% 증가. %s의 6개 환영이 혼란을 부립니다. 누가 본체인지 모르면 그만큼 불리합니다.",
					skillName, buffRatio * 100, attacker.getName()
				);
				case UNCOMMON -> log.add(
					"[%s 효과] 발동 — 명중률 +%.0f%% 증가. %s의 그림자들이 춤추듯 나타났습니다. 상대는 멘탈이 빠르게 와르르 무너집니다.",
					skillName, buffRatio * 100, attacker.getName()
				);
				case COMMON -> log.add(
					"[%s 효과] 적용 — 명중률 +%.0f%% 증가. %s의 6개 허깨비가 나타났습니다. 누가 맞았는지 상대도, 당신도 모릅니다.",
					skillName, buffRatio * 100, attacker.getName()
				);
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}
