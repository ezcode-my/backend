package org.ezcode.codetest.infrastructure.persistence.repository.game;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameCharacterRepositoryImpl implements GameCharacterRepository {

	private final GameCharacterJpaRepository characterRepository;

	public GameCharacter save(GameCharacter character) {

		return characterRepository.save(character);
	}

	public Optional<GameCharacter> findByUserId(Long userId) {

		return characterRepository.findByUserId(userId);
	}


}
