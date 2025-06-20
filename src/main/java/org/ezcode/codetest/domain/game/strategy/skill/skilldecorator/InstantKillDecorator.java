package org.ezcode.codetest.domain.game.strategy.skill.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;

public class InstantKillDecorator implements SkillStrategy {

	private final SkillStrategy delegate;
	private final Grade grade;
	private final String skillName;

	public InstantKillDecorator(SkillStrategy delegate, Grade grade, String skillName) {
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

		if (grade == Grade.LEGENDARY || grade == Grade.UNIQUE) {
			double chance = (grade == Grade.LEGENDARY) ? 0.10 : 0.05;

			if (Math.random() < chance) {
				double dealt = attacker.getAtk() * 100;
				boolean alive = defender.playerDamaged(dealt);
				log.add("[%s]의 특수효과, 치명적인 일격이 %s의 심장을 꿰뚫었습니다. %,.1f 피해.", skillName, defender.getName(), dealt);
				log.add("%s가 %s(을)를 지옥행 1순위로 예약했습니다.", attacker.getName(), defender.getName());
				return false;
			}
			return delegate.useSkill(attacker, defender, log, weaponType);

		} else {
			double critBuff = switch (grade) {
				case RARE -> 0.15;
				case UNCOMMON -> 0.10;
				case COMMON -> 0.07;
				default -> 0.0;
			};
			if (critBuff > 0.0) {
				attacker.applyCritBuff(critBuff * attacker.getCrit());
				log.add("[%s] 효과 발동. 치명타 확률 +%.0f%% 추가 증가. 운 좋으면 오늘 죽음도 피할 수 있습니다.", skillName, critBuff * 100);
			}
			return delegate.useSkill(attacker, defender, log, weaponType);
		}
	}
}
