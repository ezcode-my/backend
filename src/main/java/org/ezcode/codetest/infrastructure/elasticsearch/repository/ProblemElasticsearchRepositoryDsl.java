package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface ProblemElasticsearchRepositoryDsl {

	SearchHits<ProblemSearchDocument> findFieldsContainingKeyword(String keyword);

	SearchHits<ProblemSearchDocument> findProblemsByKeyword(String keyword);
}
