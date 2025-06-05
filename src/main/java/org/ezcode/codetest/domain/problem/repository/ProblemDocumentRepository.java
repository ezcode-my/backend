package org.ezcode.codetest.domain.problem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

// ELASTICSEARCH REPOSITORY 입니다~

public interface ProblemDocumentRepository {

	List<ProblemSearchDocument> findAllProblemByTitle(String title);

	ProblemSearchDocument save(ProblemSearchDocument problemSearch);

	List<ProblemSearchDocument> findAllProblemByKeyword(String keyword);

	Optional<ProblemSearchDocument> findById(Long id);

	void delete(ProblemSearchDocument document);

	Set<ProblemSearchDocument> findDocumentContainingKeyword(String keyword);
}
