package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.strategy.skill.SkillStrategy;
import org.springframework.stereotype.Component;

@Component
public class DefenceSkill implements SkillStrategy {

	@Override
	public SkillEffect getType() {
		return SkillEffect.DEFENCE;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {
		attacker.consumeActionPoints();
		attacker.applyDefBuff(attacker.getDef());
		log.add("%s가 방어 모드! 방어력이 2배로 상승했습니다.", attacker.getName());
		return true;
	}
}
