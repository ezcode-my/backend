package org.ezcode.codetest.infrastructure.search.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearch;
import org.ezcode.codetest.domain.problem.repository.SearchRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemElasticsearchRepositoryImpl implements SearchRepository {

	private final ProblemElasticsearchRepository searchRepository;

	public List<ProblemSearch> findAllProblemByTitle(String title) {

		return searchRepository.findAllByTitleAndIsDeleted(title, false);
	}

	public ProblemSearch save(ProblemSearch problemSearch) {

		return searchRepository.save(problemSearch);
	}

	public List<ProblemSearch> findAllProblemByDescription(String description) {

		return searchRepository.findAllByDescriptionAndIsDeleted(description, false);
	}

}
