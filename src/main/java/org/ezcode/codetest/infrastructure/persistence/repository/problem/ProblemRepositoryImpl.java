package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.ProblemSearchCondition;
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
	private final ProblemRepositoryCustom problemRepositoryCustom;

	@Override
	public Problem save(Problem problem) {
		return problemJpaRepository.save(problem);
	}

	@Override
	public Optional<Problem> findByIdNotDeleted(Long problemId) {
		return problemJpaRepository.findByIdNotDeleted(problemId);
	}

	@Override
	public Page<Problem> searchByCondition(Pageable pageable, ProblemSearchCondition searchCondition) {
		return problemRepositoryCustom.searchByCondition(pageable, searchCondition);
	}

	@Override
	public void delete(Problem problem) {
		problem.softDelete();
	}

	@Override
	public Optional<Problem> findProblemWithTestcasesById(Long problemId) {
		return problemJpaRepository.findProblemWithTestcasesById(problemId);
	}

	@Override
	public boolean existsByTitleAndIsDeletedIsFalse(String title) {
		return problemJpaRepository.existsByTitleAndIsDeletedIsFalse(title);
	}

	@Override
	public void problemCountAdjustment(Long problemId, int correctInc) {
		problemJpaRepository.incrementCount(problemId, correctInc);
	}

	@Override
	public List<Long> getProblemIdList() {
		return problemJpaRepository.findAll().stream()
			.map(Problem::getId)
			.toList();
	}
}
