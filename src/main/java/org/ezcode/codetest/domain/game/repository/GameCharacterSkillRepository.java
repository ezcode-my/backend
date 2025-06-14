package org.ezcode.codetest.domain.game.repository;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;

public interface GameCharacterSkillRepository {

	GameCharacterSkill save(GameCharacterSkill gameCharacterSkill);

	List<GameCharacterSkill> findByCharacterId(Long characterId);

	List<GameCharacterSkill> findByCharacterIdEAndEquipped(Long characterId);
}
