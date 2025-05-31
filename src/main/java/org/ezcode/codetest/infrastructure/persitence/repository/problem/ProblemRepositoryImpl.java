package org.ezcode.codetest.infrastructure.persitence.repository.problem;

import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.repository.ProblemRepository;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

	private final ProblemJpaRepository problemJpaRepository;

	@Override
	public Problem save(Problem problem) {
		return problemJpaRepository.save(problem);
	}

	@Override
	public Optional<Problem> findById(Long id) {
		return problemJpaRepository.findById(id);
	}

	@Override
	public Page<Problem> findByCategory(Category category, Pageable pageable) {
		return problemJpaRepository.findByCategory(category, pageable);
	}

	@Override
	public Page<Problem> findAll(Pageable pageable) {
		return problemJpaRepository.findAll(pageable);
	}

	@Override
	public Problem findByIdOrElseThrow(Long id) {
		return problemJpaRepository.findById(id).orElseThrow();
	}

	@Override
	public Problem delete(Problem problem) {

		problemJpaRepository.delete(problem);

		return problem;
	}
}
