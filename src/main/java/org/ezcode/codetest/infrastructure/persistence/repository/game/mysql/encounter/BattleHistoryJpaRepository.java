package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleHistoryJpaRepository extends JpaRepository<BattleHistory, Long> {

	List<BattleHistory> findByAttackerId(Long characterId);

	List<BattleHistory> findByDefenderId(Long characterId);

	List<BattleHistory> findByAttackerIdOrDefenderId(Long attackerId, Long defenderId);

	@EntityGraph(attributePaths = {"attacker", "defender"})
	List<BattleHistory> findByDefenderIdAndCreatedAtAfterOrderByCreatedAtDesc(Long defenderId,
		LocalDateTime last24Hours);
}
