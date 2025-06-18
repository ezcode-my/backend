package org.ezcode.codetest.domain.game.strategy.skill;

import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;

public interface SkillStrategy {

	SkillEffect getType();

	boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType attackerWeapon);
}
