package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.repository.RandomEncounterRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RandomEncounterRepositoryImpl implements RandomEncounterRepository {

	private final RandomEncounterJpaRepository randomEncounterRepository;

	@Override
	public Optional<RandomEncounter> findById(Long id) {

		return randomEncounterRepository.findById(id);
	}

	@Override
	@Cacheable(value = "encounters", key = "'all'")
	public List<RandomEncounter> findAllEncounters() {

		return randomEncounterRepository.findAllByActivated(true);
	}

	@Override
	public boolean existsByName(String name) {

		return randomEncounterRepository.existsByNameAndActivated(name, true);
	}

	@Override
	public Optional<RandomEncounter> findByName(String name) {

		return randomEncounterRepository.findByNameAndActivated(name, true);
	}

	@Override
	@CacheEvict(value = "encounters", allEntries = true)
	public RandomEncounter save(RandomEncounter encounter) {

		return randomEncounterRepository.save(encounter);
	}

	@Override
	@CacheEvict(value = "encounters", allEntries = true)
	public void delete(RandomEncounter encounter) {

		randomEncounterRepository.delete(encounter);
	}

	@Override
	@CacheEvict(value = "encounters", allEntries = true)
	public void deleteByName(String name) {

		randomEncounterRepository.deleteByName(name);
	}

	@Override
	public List<RandomEncounter> findAll() {

		return randomEncounterRepository.findAll();
	}

}
