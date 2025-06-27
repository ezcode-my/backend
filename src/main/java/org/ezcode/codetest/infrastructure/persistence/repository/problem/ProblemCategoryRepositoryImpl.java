package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;
import org.ezcode.codetest.domain.problem.repository.ProblemCategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemCategoryRepositoryImpl implements ProblemCategoryRepository {

	private final ProblemCategoryJpaRepository problemCategoryRepository;

	@Override
	public ProblemCategory save(ProblemCategory problemCategory) {

		return problemCategoryRepository.save(problemCategory);
	}

	@Override
	public List<ProblemCategory> saveAll(List<ProblemCategory> problemCategories) {

		return problemCategoryRepository.saveAll(problemCategories);
	}

	@Override
	public List<ProblemCategory> findByProblemIdsIn(List<Long> problemIds) {

		return problemCategoryRepository.findByProblemIdIn(problemIds);
	}

	@Override
	public void deleteAllByProblemId(Long problemId) {

		problemCategoryRepository.deleteAllByProblemId(problemId);
		problemCategoryRepository.flush();
	}

	@Override
	@Cacheable(value = "problemCategory", key = "#problemId")
	public List<ProblemCategory> findByProblemId(Long problemId) {

		return problemCategoryRepository.findByProblemId(problemId);
	}
}
