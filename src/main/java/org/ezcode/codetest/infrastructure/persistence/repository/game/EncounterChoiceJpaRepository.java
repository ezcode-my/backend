package org.ezcode.codetest.infrastructure.persistence.repository.game;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterChoiceJpaRepository extends JpaRepository<EncounterChoice, Long> {

	boolean existsByName(String name);

	Optional<EncounterChoice> findByName(String name);

	void deleteByName(String name);
}
