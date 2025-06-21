package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacterMatchTokenBucket;
import org.ezcode.codetest.domain.game.repository.MatchTokenBucketRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MatchTokenBucketRepositoryImpl implements MatchTokenBucketRepository {

	private final MatchTokenBucketJpaRepository matchTokenBucketRepository;

	@Override
	public GameCharacterMatchTokenBucket save(GameCharacterMatchTokenBucket bucket) {

		return matchTokenBucketRepository.save(bucket);
	}

	@Override
	public Optional<GameCharacterMatchTokenBucket> findByCharacterId(Long characterId) {

		return matchTokenBucketRepository.findByCharacterId(characterId);
	}

}
