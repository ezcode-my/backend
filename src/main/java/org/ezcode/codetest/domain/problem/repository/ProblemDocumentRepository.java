package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

// ELASTICSEARCH REPOSITORY 입니다~

public interface ProblemDocumentRepository {

	List<ProblemSearchDocument> findAllProblemByTitle(String title);

	ProblemSearchDocument save(ProblemSearchDocument problemSearch);

	List<ProblemSearchDocument> findAllProblemByDescription(String description);
}
