package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProblemElasticsearchRepository extends
	ElasticsearchRepository<ProblemSearchDocument, Long>,
	ProblemElasticsearchRepositoryDsl {

	@Query("""
		{
		  "bool": {
		    "filter": [
		      { "term": { "isDeleted": false } }
		    ],
		    "should": [
		      { "match":       { "title":             { "query": "?0", "boost": 12 } } },
		      { "match":       { "description":       { "query": "?0", "boost": 5  } } },
		      { "match":       { "category":          { "query": "?0", "boost": 5  } } },
		      { "match":       { "difficulty":        { "query": "?0", "boost": 3  } } },
		      { "match":       { "reference":         { "query": "?0", "boost": 5  } } },
		      { "term":        { "title.keyword":     { "value": "?0", "boost": 20 } } },
		      { "term":        { "description.keyword": { "value": "?0", "boost": 16  } } },
		      { "term":        { "category.keyword":  { "value": "?0", "boost": 14  } } },
		      { "term":        { "difficulty.keyword": { "value": "?0", "boost": 13  } } },
		      { "term":        { "reference.keyword":  { "value": "?0", "boost": 12  } } }
		    ],
		    "minimum_should_match": 1
		  }
		}
		""")
	List<ProblemSearchDocument> findAllByKeyword(String keyword);

}
