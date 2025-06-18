package org.ezcode.codetest.domain.game.strategy.encounter;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;

public interface EncounterStrategy {

	RandomEncounterEffect getEffect();

	void eventHappen(GameCharacter character, Inventory playerInventory, CharacterContext playerContext,
		EncounterLog log);
}
