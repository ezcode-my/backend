package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;

import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class MentalDestroySkill extends AbstractSkill {

	@Override
	public SkillEffect getType() { return SkillEffect.MENTAL_DESTROY; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		double chance = 70.0;
		if (RNG.nextDouble() * 100 < chance) {
			int reduced = 0;
			for (int i = 0; i < 2; i++) {
				if (defender.consumeActionPoints()) {
					reduced++;
				} else {
					break;
				}
			}
			log.add("%s의 정신 파괴! %s의 행동력이 %d 감소했습니다.", attacker.getName(), defender.getName(), reduced);
		} else {
			log.add("%s의 정신 파괴 시도! 그러나 실패했습니다.", attacker.getName());
		}
		return true;
	}
}
