package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GameCharacterRepositoryImpl {

	private final GameCharacterJpaRepository characterRepository;


	public GameCharacter save(GameCharacter character) {

		return characterRepository.save(character);
	}


}
