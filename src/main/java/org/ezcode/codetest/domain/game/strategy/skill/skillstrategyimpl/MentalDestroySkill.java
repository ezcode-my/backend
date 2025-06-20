package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class MentalDestroySkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.MENTAL_DESTROY;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		attacker.consumeActionPoints();

		double chance = 50.0;
		if (RNG.nextDouble() * 100 < chance) {
			defender.consumeActionPoints();
			log.add("%s가 %s의 정신을 조용히 산산조각냈습니다. - AP 1 감소, 남은 AP %d", attacker.getName(),
				defender.getName(), defender.getAp());
		} else {
			log.add("%s가 %s의 머리를 두드렸지만, 아직 멀쩡한 모양입니다. 정신 붕괴 실패.", attacker.getName(), defender.getName());
		}
		return true;
	}
}
