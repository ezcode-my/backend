package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
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

		long count = characterRepository.count();
		if (count == 0) return Optional.empty();

		for (int i = 0; i < 5; i++) {

			long randomId = ThreadLocalRandom.current().nextLong(1, count + 1);

			Optional<GameCharacter> optional = characterRepository.findById(randomId);

			if (optional.isEmpty()) continue;

			GameCharacter character = optional.get();

			if (!character.getUser().getId().equals(userId)) {
				return Optional.of(character);
			}
		}

		return Optional.empty();
	}

}
