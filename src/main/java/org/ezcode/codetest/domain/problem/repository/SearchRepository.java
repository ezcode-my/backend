package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearch;

public interface SearchRepository {

	List<ProblemSearch> findAllProblemByTitle(String title);

	ProblemSearch save(ProblemSearch problemSearch);

	List<ProblemSearch> findAllProblemByDescription(String description);
}
