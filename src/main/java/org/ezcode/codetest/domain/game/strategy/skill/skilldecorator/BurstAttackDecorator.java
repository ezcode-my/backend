package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

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
	public SkillEffect getType() {
		return delegate.getType();
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		if (weaponType != WeaponType.SHOT_GUN) {
			attacker.consumeActionPoints();
			log.add("%s : 버스트 어택 실패. 산탄총이 아닙니다. 무기를 바꾸시죠, 의사 선생님.", attacker.getName());
			return true;
		}

		double accBuffRatio = switch (grade) {
			case LEGENDARY -> 0.30;
			case UNIQUE -> 0.20;
			case RARE -> 0.10;
			case UNCOMMON -> 0.07;
			case COMMON -> 0.03;
			default -> 0.0;
		};

		if (accBuffRatio > 0.0) {
			double accBuff = attacker.getAccuracy() * accBuffRatio;
			attacker.applyAccuracyBuff(accBuff);

			switch (grade) {
				case LEGENDARY -> log.add(
					"[%s] 작동 개시. 명중률이 +%.1f%% 추가 증가. 이제 당신의 총알은 해부학 교과서입니다.",
					skillName, accBuffRatio * 100
				);
				case UNIQUE -> log.add(
					"[%s] 활성화. 명중률이 +%.1f%% 추가 증가. 리로드는 필요 없습니다. 그냥 웃으세요.",
					skillName, accBuffRatio * 100
				);
				case RARE -> log.add(
					"[%s] 장착 완료. 명중률이 +%.1f%% 추가 증가. 예술적인 점사로 적을 뻘건색으로 칠합니다.",
					skillName, accBuffRatio * 100
				);
				case UNCOMMON -> log.add(
					"[%s] 발동. 명중률이 +%.1f%% 추가 증가. 당신의 언어는 12게이지입니다.",
					skillName, accBuffRatio * 100
				);
				case COMMON -> log.add(
					"[%s] 적용됨. 명중률이 +%.1f%% 추가 증가. 이제 아무도 말대꾸하지 않습니다.",
					skillName, accBuffRatio * 100
				);
			}
		}

		return delegate.useSkill(attacker, defender, log, weaponType);
	}
}

