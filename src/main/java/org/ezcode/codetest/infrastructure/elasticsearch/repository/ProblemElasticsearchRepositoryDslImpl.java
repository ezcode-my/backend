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
			.withQuery(q -> q.bool(b -> b
				.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
				.should(s -> s.match(m -> m.field("title").query(keyword).boost(12f)))
				.should(s -> s.match(m -> m.field("description").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("category").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("difficulty").query(keyword).boost(3f)))
				.should(s -> s.match(m -> m.field("reference").query(keyword).boost(5f)))

				.minimumShouldMatch("1")
			))
			.withHighlightQuery(
				new HighlightQuery(
					new Highlight(
						HighlightParameters.builder()
							.withPreTags("")
							.withPostTags("")
							.withFragmentSize(20)
							.withNumberOfFragments(1)
							.build(),
						List.of(
							new HighlightField("title"),
							new HighlightField("category"),
							new HighlightField("difficulty"),
							new HighlightField("reference"),
							new HighlightField("description")
						)
					),
					ProblemSearchDocument.class
				)
			)
			.withSourceFilter(
				new FetchSourceFilterBuilder().withIncludes().build()
			)
			.build();

		return elasticsearchOperations.search(searchQuery, ProblemSearchDocument.class);
	}
}