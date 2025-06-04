package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemElasticsearchAdapter implements ProblemDocumentRepository {

	private final ProblemElasticsearchRepository searchRepository;

	public List<ProblemSearchDocument> findAllProblemByTitle(String title) {

		return searchRepository.findAllByTitleAndIsDeleted(title, false);
	}

	public ProblemSearchDocument save(ProblemSearchDocument problemSearch) {

		return searchRepository.save(problemSearch);
	}

	public List<ProblemSearchDocument> findAllProblemByDescription(String description) {

		return searchRepository.findAllByDescriptionAndIsDeleted(description, false);
	}
}
