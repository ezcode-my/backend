package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.GameCharacter;

public interface GameCharacterRepository {

	GameCharacter save(GameCharacter character);

	Optional<GameCharacter> findByUserId(Long userId);
}
