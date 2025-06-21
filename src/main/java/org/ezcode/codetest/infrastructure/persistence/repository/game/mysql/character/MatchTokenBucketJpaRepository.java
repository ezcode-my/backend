package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.character;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.character.GameCharacterMatchTokenBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface MatchTokenBucketJpaRepository extends JpaRepository<GameCharacterMatchTokenBucket, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<GameCharacterMatchTokenBucket> findByCharacterId(Long characterId);

}
