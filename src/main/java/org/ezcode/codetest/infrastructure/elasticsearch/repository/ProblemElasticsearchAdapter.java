package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;
import java.util.Optional;

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

	public Optional<ProblemSearchDocument> findById(Long id) {

		return searchRepository.findById(id);
	}

	public void delete(ProblemSearchDocument document) {

		document.softDelete();

		save(document); //es 는 더티체킹이 안됨
	}
}
