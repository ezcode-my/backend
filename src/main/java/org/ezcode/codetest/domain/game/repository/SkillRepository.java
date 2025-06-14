package org.ezcode.codetest.domain.game.repository;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.Skill;

public interface SkillRepository {

	Optional<Skill> findById(Long id);

	boolean existsByName(String name);

	Optional<Skill> findByName(String name);

	Skill save(Skill skill);

	void delete(Skill skill);

	void deleteByName(String name);

	List<Skill> findAll();
}
