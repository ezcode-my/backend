package org.ezcode.codetest.infrastructure.persistence.repository.game;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SkillRepositoryImpl implements SkillRepository {

	private final SkillJpaRepository skillRepository;

	@Override
	public Optional<Skill> findById(Long id) {

		return skillRepository.findById(id);
	}

	@Override
	public boolean existsByName(String name) {

		return skillRepository.existsByName(name);
	}

	@Override
	public Optional<Skill> findByName(String name) {

		return skillRepository.findByName(name);
	}

	@Override
	public Skill save(Skill skill) {

		return skillRepository.save(skill);
	}

	@Override
	public void delete(Skill skill) {

		skillRepository.delete(skill);
	}

	@Override
	public void deleteByName(String name) {

		skillRepository.deleteByName(name);
	}

	@Override
	public List<Skill> findAll() {

		return skillRepository.findAll();
	}
}
