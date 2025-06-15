package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.repository.RandomEncounterRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RandomEncounterRepositoryImpl implements RandomEncounterRepository {

	private final RandomEncounterJpaRepository randomEncounterRepository;



}
