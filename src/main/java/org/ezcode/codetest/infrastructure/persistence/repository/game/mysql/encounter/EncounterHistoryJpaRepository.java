package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.List;

import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterHistoryJpaRepository extends JpaRepository<EncounterHistory, Long> {

	List<EncounterHistory> findByCharacterId(Long characterId);
}
