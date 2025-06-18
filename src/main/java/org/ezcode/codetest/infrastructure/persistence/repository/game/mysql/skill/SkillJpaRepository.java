package org.ezcode.codetest.infrastructure.persistence.repository.game.mysql.skill;

import java.util.Optional;

import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillJpaRepository extends JpaRepository<Skill, Long> {

	Optional<Skill> findByName(String name);

	void deleteByName(String name);

	boolean existsByName(String name);
}
