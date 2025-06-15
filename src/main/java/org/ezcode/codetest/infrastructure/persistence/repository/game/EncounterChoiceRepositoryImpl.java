package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.repository.EncounterRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EncounterChoiceRepositoryImpl implements EncounterRepository {

	private final EncounterChoiceJpaRepository encounterRepository;

}
