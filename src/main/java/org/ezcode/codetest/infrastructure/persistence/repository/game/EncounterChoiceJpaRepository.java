package org.ezcode.codetest.infrastructure.persistence.repository.game;

import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterChoiceJpaRepository extends JpaRepository<EncounterChoice, Long> {
}
