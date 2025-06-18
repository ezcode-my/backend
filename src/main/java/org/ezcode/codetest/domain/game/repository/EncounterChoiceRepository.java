package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;

public interface EncounterChoiceRepository {

	Optional<EncounterChoice> findById(Long id);

	boolean existsByName(String name);

	List<EncounterChoice> findChoiceByPlayerDecision(Long encounterId, boolean playerDecision);

	Optional<EncounterChoice> findByName(String name);

	EncounterChoice save(EncounterChoice encounter);

	void delete(EncounterChoice encounter);

	void deleteByName(String name);

	List<EncounterChoice> findAll();
}
