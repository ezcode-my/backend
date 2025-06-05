package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

@Repository
@RequiredArgsConstructor
public class ProblemElasticsearchRepositoryDslImpl implements ProblemElasticsearchRepositoryDsl {

	private final ElasticsearchOperations elasticsearchOperations;

	public SearchHits<ProblemSearchDocument> findFieldsContainingKeyword(String keyword) {

		Query searchQuery = NativeQuery.builder()
			.withQuery(q -> q.multiMatch(m -> m
				.fields(List.of("title", "category", "difficulty", "reference", "description"))
				.query(keyword)
			))
			.withHighlightQuery(
				new HighlightQuery(
					new Highlight(
						HighlightParameters.builder()
							.withPreTags("<em>").withPostTags("</em>")
							.withFragmentSize(150).withNumberOfFragments(1)
							.build(),
						List.of(
							new HighlightField("description")
						)
					),
					ProblemSearchDocument.class  // 하이라이트 필드들이 속한 엔티티 타입
				)
			)
			.withSourceFilter(
				new FetchSourceFilterBuilder().withIncludes("title", "category", "difficulty", "reference").build()
			)
			.build();

		return elasticsearchOperations.search(searchQuery, ProblemSearchDocument.class);
	}
}