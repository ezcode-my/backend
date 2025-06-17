package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Encounter.RandomEncounter;

public interface RandomEncounterRepository {

	Optional<RandomEncounter> findById(Long id);

	boolean existsByName(String name);

	Optional<RandomEncounter> findByName(String name);

	RandomEncounter save(RandomEncounter encounter);

	void delete(RandomEncounter encounter);

	void deleteByName(String name);

	List<RandomEncounter> findAll();

}
