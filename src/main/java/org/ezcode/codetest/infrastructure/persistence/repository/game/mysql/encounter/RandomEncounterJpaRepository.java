package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RandomEncounterJpaRepository extends JpaRepository<RandomEncounter, Long> {

	boolean existsByNameAndActivated(String name, boolean isActivated);

	Optional<RandomEncounter> findByNameAndActivated(String name, boolean isActivated);

	void deleteByName(String name);

	List<RandomEncounter> findAllByActivated(boolean activated);
}
