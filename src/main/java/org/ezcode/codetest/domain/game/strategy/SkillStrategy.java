package org.ezcode.codetest.domain.game.strategy;

import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;

public interface SkillStrategy {

	SkillEffect getType();

	boolean useSkill(CharacterContext player, CharacterContext enemy, BattleLog log);
}
