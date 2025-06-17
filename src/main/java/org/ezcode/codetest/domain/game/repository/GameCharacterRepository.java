package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Character.GameCharacter;

public interface GameCharacterRepository {

	GameCharacter save(GameCharacter character);

	Optional<GameCharacter> findByUserId(Long userId);

	List<GameCharacter> findRandomCharacter(Long userId);
}
