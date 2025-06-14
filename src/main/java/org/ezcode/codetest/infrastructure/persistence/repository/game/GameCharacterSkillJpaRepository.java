package org.ezcode.codetest.infrastructure.persistence.repository.game;

import java.util.List;

import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.enums.SkillSlotType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCharacterSkillJpaRepository extends JpaRepository<GameCharacterSkill, Long> {

	List<GameCharacterSkill> findByCharacterId(Long characterId);

	@EntityGraph(attributePaths = "skill")
	List<GameCharacterSkill> findByCharacterIdAndSlotType(Long characterId, SkillSlotType type);
}
