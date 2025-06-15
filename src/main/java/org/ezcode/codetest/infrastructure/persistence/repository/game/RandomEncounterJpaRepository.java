package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.RandomEncounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomEncounterJpaRepository extends JpaRepository<RandomEncounter, Long> {
}
