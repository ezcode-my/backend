package org.ezcode.codetest.domain.game.repository;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacterMatchTokenBucket;

public interface MatchTokenBucketRepository {

	GameCharacterMatchTokenBucket save(GameCharacterMatchTokenBucket bucket);

	Optional<GameCharacterMatchTokenBucket> findByCharacterId(Long characterId);

}
