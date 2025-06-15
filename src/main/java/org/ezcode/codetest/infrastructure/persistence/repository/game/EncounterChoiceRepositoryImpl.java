package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.repository.EncounterChoiceRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EncounterChoiceRepositoryImpl implements EncounterChoiceRepository {

	private final EncounterChoiceJpaRepository encounterRepository;

}
