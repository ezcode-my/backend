package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemCategory;

public interface ProblemCategoryRepository {

	ProblemCategory save(ProblemCategory problemCategory);

	List<ProblemCategory> saveAll(List<ProblemCategory> problemCategories);

	List<ProblemCategory> findByProblemIdsIn(List<Long> problemIds);

	void deleteAllByProblemId(Long problemId);

	List<ProblemCategory> findByProblemId(Long problemId);
}

