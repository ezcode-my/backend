package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.encounter;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncounterChoiceJpaRepository extends JpaRepository<EncounterChoice, Long> {

	boolean existsByName(String name);

	Optional<EncounterChoice> findByName(String name);

	void deleteByName(String name);

	List<EncounterChoice> findByEncounterIdAndPlayerDecision(Long encounterId, boolean playerDecision);
}
