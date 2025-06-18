package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;
import org.ezcode.codetest.domain.game.repository.EncounterHistoryRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EncounterHistoryRepositoryImpl implements EncounterHistoryRepository {

	private final EncounterHistoryJpaRepository encounterHistoryRepository;

	@Override
	public Optional<EncounterHistory> findById(Long id) {

		return encounterHistoryRepository.findById(id);
	}

	@Override
	public List<EncounterHistory> findByPlayerId(Long characterId) {

		return encounterHistoryRepository.findByCharacterId(characterId);
	}

	@Override
	public EncounterHistory save(EncounterHistory battleHistory) {

		return encounterHistoryRepository.save(battleHistory);
	}

	@Override
	public void delete(EncounterHistory battleHistory) {

		encounterHistoryRepository.delete(battleHistory);
	}

	@Override
	public List<EncounterHistory> findAll() {

		return encounterHistoryRepository.findAll();
	}

}
