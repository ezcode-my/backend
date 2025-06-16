package org.ezcode.codetest.infrastructure.persistence.repository.game;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;
import org.ezcode.codetest.domain.game.model.entity.RandomEncounter;
import org.ezcode.codetest.domain.game.repository.EncounterChoiceRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EncounterChoiceRepositoryImpl implements EncounterChoiceRepository {

	private final EncounterChoiceJpaRepository encounterRepository;

	@Override
	public Optional<EncounterChoice> findById(Long id) {

		return encounterRepository.findById(id);
	}

	@Override
	public boolean existsByName(String name) {

		return encounterRepository.existsByName(name);
	}

	@Override
	public Optional<EncounterChoice> findByName(String name) {

		return encounterRepository.findByName(name);
	}

	@Override
	public EncounterChoice save(EncounterChoice encounter) {

		return encounterRepository.save(encounter);
	}

	@Override
	public void delete(EncounterChoice encounter) {

		encounterRepository.delete(encounter);
	}

	@Override
	public void deleteByName(String name) {

		encounterRepository.deleteByName(name);
	}

	@Override
	public List<EncounterChoice> findAll() {

		return encounterRepository.findAll();
	}

}
