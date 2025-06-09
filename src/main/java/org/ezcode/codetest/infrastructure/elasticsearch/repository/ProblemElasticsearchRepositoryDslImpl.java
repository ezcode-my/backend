package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
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
				.should(s -> s.match(m -> m.field("description").query(keyword).boost(3f)))
				.should(s -> s.match(m -> m.field("category").query(keyword).boost(6f)))
				.should(s -> s.match(m -> m.field("difficulty").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("reference").query(keyword).boost(7f)))
				.should(s -> s.match(m -> m.field("referenceKor").query(keyword).boost(7f)))
				.should(s -> s.match(m -> m.field("categoryKor").query(keyword).boost(6f)))
				.should(s -> s.match(m -> m.field("difficultyEn").query(keyword).boost(5f)))
				.minimumShouldMatch("1")
			))
			.withPageable(PageRequest.of(0, 25))
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
							new HighlightField("description"),
							new HighlightField("categoryKor"),
							new HighlightField("referenceKor"),
							new HighlightField("difficultyEn")
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

	public SearchHits<ProblemSearchDocument> findProblemsByKeyword(String keyword) {

		Query exactQuery = NativeQuery.builder()
			.withQuery(q -> q.bool(b -> b
				.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
				.filter(f2 -> f2.bool(bs -> bs
					.should(s -> s.term(t -> t.field("title.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("category.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("difficulty.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("reference.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("difficultyEn.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("categoryKor.keyword").value(keyword)))
					.should(s -> s.term(t -> t.field("referenceKor.keyword").value(keyword)))
					.minimumShouldMatch("1")
				))
			))
			.withPageable(PageRequest.of(0, 30))
			.withSourceFilter(new FetchSourceFilterBuilder()
				.withIncludes("title", "description", "category", "difficulty", "reference")
				.build()
			)
			.build();

		SearchHits<ProblemSearchDocument> exactHits =
			elasticsearchOperations.search(exactQuery, ProblemSearchDocument.class);

		if (!exactHits.isEmpty()) {
			return exactHits;
		}

		Query fuzzyQuery = NativeQuery.builder()
			.withQuery(q -> q.bool(b -> b
				.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
				.should(s -> s.match(m -> m.field("title").query(keyword).boost(12f)))
				.should(s -> s.match(m -> m.field("description").query(keyword).boost(4f)))
				.should(s -> s.match(m -> m.field("category").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("difficulty").query(keyword).boost(3f)))
				.should(s -> s.match(m -> m.field("reference").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("categoryKor").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("difficultyEn").query(keyword).boost(3f)))
				.should(s -> s.match(m -> m.field("referenceKor").query(keyword).boost(5f)))
				.minimumShouldMatch("1")
			))
			.withPageable(PageRequest.of(0, 40))
			.withSourceFilter(new FetchSourceFilterBuilder()
				.withIncludes("title", "description", "category", "difficulty", "reference")
				.build()
			)
			.build();

		return elasticsearchOperations.search(fuzzyQuery, ProblemSearchDocument.class);
	}
}