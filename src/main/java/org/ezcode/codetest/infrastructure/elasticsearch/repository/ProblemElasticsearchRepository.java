package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemElasticsearchRepository extends ElasticsearchRepository<ProblemSearchDocument, Long> {

	List<ProblemSearchDocument> findAllByTitleAndIsDeleted(String title, Boolean isDeleted);

	List<ProblemSearchDocument> findAllByDescriptionAndIsDeleted(String description, Boolean isDeleted);
}
