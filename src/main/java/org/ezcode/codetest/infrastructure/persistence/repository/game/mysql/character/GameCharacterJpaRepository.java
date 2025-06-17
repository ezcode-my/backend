package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Character.GameCharacter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCharacterJpaRepository extends JpaRepository<GameCharacter, Long> {

	@EntityGraph(attributePaths = "stats")
	Optional<GameCharacter> findByUserId(Long userId);
}
