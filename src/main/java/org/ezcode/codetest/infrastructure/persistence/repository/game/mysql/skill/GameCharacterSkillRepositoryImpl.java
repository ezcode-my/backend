package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.skill;

import java.util.List;

import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;
import org.ezcode.codetest.domain.game.repository.GameCharacterSkillRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameCharacterSkillRepositoryImpl implements GameCharacterSkillRepository {

	private final GameCharacterSkillJpaRepository gameCharacterSkillRepository;

	@Override
	public GameCharacterSkill save(GameCharacterSkill gameCharacterSkill) {

		return gameCharacterSkillRepository.save(gameCharacterSkill);
	}

	@Override
	public List<GameCharacterSkill> findByCharacterId(Long characterId) {

		return gameCharacterSkillRepository.findByCharacterId(characterId);
	}

	@Override
	public List<GameCharacterSkill> findByCharacterIdAndEquipped(Long characterId) {

		return gameCharacterSkillRepository.findByCharacterIdAndSlotTypeNot(characterId, SkillSlotType.BACKPACK);
	}

	@Override
	public List<GameCharacterSkill> findByCharacterIdAndUnEquipped(Long characterId) {

		return gameCharacterSkillRepository.findByCharacterIdAndSlotType(characterId, SkillSlotType.BACKPACK);
	}

	@Override
	public boolean existsByCharacterIdAndSkillId(Long characterId, Long skillId) {

		return gameCharacterSkillRepository.existsByCharacterIdAndSkillId(characterId, skillId);
	}

}
