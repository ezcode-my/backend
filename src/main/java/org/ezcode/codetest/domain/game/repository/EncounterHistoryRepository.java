package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;

public interface EncounterHistoryRepository {

	Optional<EncounterHistory> findById(Long id);

	List<EncounterHistory> findByPlayerId(Long characterId);

	EncounterHistory save(EncounterHistory battleHistory);

	void delete(EncounterHistory battleHistory);

	List<EncounterHistory> findAll();
}
