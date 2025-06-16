package org.ezcode.codetest.domain.game.strategy;

import org.ezcode.codetest.domain.game.model.enums.SkillEffect;
import org.ezcode.codetest.domain.game.model.vo.CharacterContext;

public interface SkillStrategy {

	SkillEffect getType();

	void useSkill(CharacterContext player, CharacterContext enemy);
}
