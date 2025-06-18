package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameCharacterRepositoryImpl implements GameCharacterRepository {

	private final GameCharacterJpaRepository characterRepository;

	@Override
	public GameCharacter save(GameCharacter character) {

		return characterRepository.save(character);
	}

	@Override
	public Optional<GameCharacter> findByUserId(Long userId) {

		return characterRepository.findByUserId(userId);
	}

	@Override
	public Optional<GameCharacter> findRandomCharacter(Long userId) {

		GameCharacter randomCharacter = characterRepository
			.findRandomCharacter(userId, PageRequest.of(0, 1)).get(0);

		return Optional.of(randomCharacter);
	}

}
