package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;

public interface GameCharacterRepository {

	GameCharacter save(GameCharacter character);

	Optional<GameCharacter> findByUserId(Long userId);

	Optional<GameCharacter> findRandomCharacter(Long userId);

	boolean isCharacterExist(Long userId);
}
