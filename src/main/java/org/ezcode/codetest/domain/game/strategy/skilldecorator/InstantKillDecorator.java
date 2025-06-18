package org.ezcode.codetest.domain.game.strategy.skilldecorator;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;

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
	public SkillEffect getType() { return delegate.getType(); }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (grade == Grade.LEGENDARY || grade == Grade.UNIQUE) {
			double chance = (grade == Grade.LEGENDARY) ? 0.25 : 0.05;

			if (Math.random() < chance) {
					double dealt = attacker.getAtk() * 100;
					boolean alive = defender.playerDamaged(dealt);
					log.add("[%s] 치명적인 일격이 %s의 심장을 꿰뚫었습니다 %,.1f 피해.", skillName, defender.getName(), dealt);
					log.add("%s이(가) %s를 즉시 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
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
				log.add("[%s] 효과로 크리티컬 확률 +%.0f%% 추가 적용", skillName, critBuff * 100);
			}
			return delegate.useSkill(attacker, defender, log, weaponType);
		}
	}
}
