package org.ezcode.codetest.infrastructure.search.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemElasticsearchRepository extends ElasticsearchRepository<ProblemSearch, Long> {

	List<ProblemSearch> findAllByTitleAndIsDeleted(String title, Boolean isDeleted);

	List<ProblemSearch> findAllByDescriptionAndIsDeleted(String description, Boolean isDeleted);
}
