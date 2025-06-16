package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;

public interface EncounterChoiceRepository {

	Optional<EncounterChoice> findById(Long id);

	boolean existsByName(String name);

	Optional<EncounterChoice> findByName(String name);

	EncounterChoice save(EncounterChoice encounter);

	void delete(EncounterChoice encounter);

	void deleteByName(String name);

	List<EncounterChoice> findAll();
}
