package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProblemElasticsearchRepository extends
	ElasticsearchRepository<ProblemSearchDocument, Long>,
	ProblemElasticsearchRepositoryDsl {

	List<ProblemSearchDocument> findAllByTitleAndIsDeleted(String title, Boolean isDeleted);

	@Query("""
		{
		  "bool": {
		    "filter": [
		      { "term": { "isDeleted": false } }
		    ],
		    "should": [
		      { "match": { "title":       { "query": "?0", "boost": 12 } } },
		      { "match": { "description": { "query": "?0", "boost": 5 } } },
		      { "match": { "category":    { "query": "?0", "boost": 5 } } },
		      { "match": { "difficulty":  { "query": "?0", "boost": 3 } } },
		      { "match": { "reference":   { "query": "?0", "boost": 5 } } }
		    ],
		    "minimum_should_match": 1
		  }
		}
		""")
	List<ProblemSearchDocument> findAllByKeyword(String keyword);
}
