package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;

public interface BattleHistoryRepository {

	Optional<BattleHistory> findById(Long id);

	List<BattleHistory> findByAttackerId(Long characterId);

	List<BattleHistory> findByDefenderId(Long characterId);

	List<BattleHistory> findByCharacterId(Long characterId);

	BattleHistory save(BattleHistory battleHistory);

	void delete(BattleHistory battleHistory);

	List<BattleHistory> findAll();

	List<BattleHistory> findCreatedInLast24Hours(Long playerId);
}
