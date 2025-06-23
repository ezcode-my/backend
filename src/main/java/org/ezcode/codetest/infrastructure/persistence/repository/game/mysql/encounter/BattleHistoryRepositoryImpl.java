package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;
import org.ezcode.codetest.domain.game.repository.BattleHistoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BattleHistoryRepositoryImpl implements BattleHistoryRepository {

	private final BattleHistoryJpaRepository battleHistoryRepository;

	@Override
	public Optional<BattleHistory> findById(Long id) {

		return battleHistoryRepository.findById(id);
	}

	@Override
	public List<BattleHistory> findByAttackerId(Long characterId) {

		return battleHistoryRepository.findByAttackerId(characterId);
	}

	@Override
	public List<BattleHistory> findByDefenderId(Long characterId) {

		return battleHistoryRepository.findByDefenderId(characterId);
	}

	@Override
	public List<BattleHistory> findByCharacterId(Long characterId) {

		return battleHistoryRepository.findByAttackerIdOrDefenderId(characterId, characterId);
	}

	@Override
	public BattleHistory save(BattleHistory battleHistory) {

		return battleHistoryRepository.save(battleHistory);
	}

	@Override
	public void delete(BattleHistory battleHistory) {

		battleHistoryRepository.delete(battleHistory);
	}

	@Override
	public List<BattleHistory> findAll() {

		return battleHistoryRepository.findAll();
	}

	@Override
	@Cacheable(value = "histories", key = "#playerId")
	public List<BattleHistory> findCreatedInLast24Hours(Long playerId) {

		return battleHistoryRepository.findByDefenderIdAndCreatedAtAfterOrderByCreatedAtDesc(playerId,
			LocalDateTime.now().minusDays(1));
	}
}
