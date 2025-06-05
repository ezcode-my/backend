package org.ezcode.codetest.domain.problem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

public interface ProblemDocumentRepository {

	ProblemSearchDocument save(ProblemSearchDocument problemSearch);

	List<ProblemSearchDocument> findAllByKeyword(String keyword);

	Optional<ProblemSearchDocument> findById(Long id);

	void delete(ProblemSearchDocument document);

	Set<ProblemSearchDocument> findDocumentContainingKeyword(String keyword);
}
