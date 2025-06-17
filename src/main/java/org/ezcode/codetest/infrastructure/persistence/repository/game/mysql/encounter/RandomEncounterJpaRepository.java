package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.Encounter.RandomEncounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomEncounterJpaRepository extends JpaRepository<RandomEncounter, Long> {

	boolean existsByNameAndActivated(String name, boolean isActivated);

	Optional<RandomEncounter> findByNameAndActivated(String name, boolean isActivated);

	void deleteByName(String name);
}
