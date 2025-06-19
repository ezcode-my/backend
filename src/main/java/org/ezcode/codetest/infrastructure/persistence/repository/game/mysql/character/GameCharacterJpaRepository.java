package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;


import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.NonNull;

public interface GameCharacterJpaRepository extends JpaRepository<GameCharacter, Long> {

	@EntityGraph(attributePaths = "stats")
	Optional<GameCharacter> findByUserId(Long userId);

	@EntityGraph(attributePaths = "stats")
	@NonNull
	Optional<GameCharacter> findById(@NonNull Long id);

	@Cacheable(value = "counts", key = "'count'")
	long count();
}
