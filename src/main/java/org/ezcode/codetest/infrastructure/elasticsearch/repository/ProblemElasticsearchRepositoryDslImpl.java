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
				.should(s -> s.match(m -> m.field("description").query(keyword).boost(3f)))
				.should(s -> s.match(m -> m.field("category").query(keyword).boost(6f)))
				.should(s -> s.match(m -> m.field("difficulty").query(keyword).boost(5f)))
				.should(s -> s.match(m -> m.field("reference").query(keyword).boost(7f)))
				.should(s -> s.match(m -> m.field("referenceKor").query(keyword).boost(7f)))
				.should(s -> s.match(m -> m.field("categoryKor").query(keyword).boost(6f)))
				.should(s -> s.match(m -> m.field("difficultyEn").query(keyword).boost(5f)))
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
		Query searchQuery = NativeQuery.builder()
			.withQuery(q -> q.bool(b -> b
				.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
				.should(s -> s.term(t -> t.field("title.keyword").value(keyword).boost(40f)))
				.should(s -> s.term(t -> t.field("description.keyword").value(keyword).boost(40f)))
				.should(s -> s.term(t -> t.field("category.keyword").value(keyword).boost(35f)))
				.should(s -> s.term(t -> t.field("difficulty.keyword").value(keyword).boost(28f)))
				.should(s -> s.term(t -> t.field("reference.keyword").value(keyword).boost(32f)))
				.should(s -> s.term(t -> t.field("difficultyEn.keyword").value(keyword).boost(35f)))
				.should(s -> s.term(t -> t.field("categoryKor.keyword").value(keyword).boost(28f)))
				.should(s -> s.term(t -> t.field("referenceKor.keyword").value(keyword).boost(32f)))
				.should(s -> s.bool(bs -> bs
					.should(ss -> ss.match(m -> m.field("title").query(keyword).boost(12f)))
					.should(ss -> ss.match(m -> m.field("description").query(keyword).boost(5f)))
					.should(ss -> ss.match(m -> m.field("category").query(keyword).boost(5f)))
					.should(ss -> ss.match(m -> m.field("difficulty").query(keyword).boost(3f)))
					.should(ss -> ss.match(m -> m.field("reference").query(keyword).boost(5f)))
					.should(ss -> ss.match(m -> m.field("categoryKor").query(keyword).boost(5f)))
					.should(ss -> ss.match(m -> m.field("difficultyEn").query(keyword).boost(3f)))
					.should(ss -> ss.match(m -> m.field("referenceKor").query(keyword).boost(5f)))
					.minimumShouldMatch("1")
					.mustNot(mn -> mn.term(t -> t.field("title.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("description.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("category.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("difficulty.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("reference.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("difficultyEn.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("categoryKor.keyword").value(keyword)))
					.mustNot(mn -> mn.term(t -> t.field("referenceKor.keyword").value(keyword)))
				))
				.minimumShouldMatch("1")
			))
			.withSourceFilter(
				new FetchSourceFilterBuilder()
					.withIncludes("title", "description", "category", "difficulty", "reference")
					.build()
			)
			.build();

		return elasticsearchOperations.search(searchQuery, ProblemSearchDocument.class);
	}
}