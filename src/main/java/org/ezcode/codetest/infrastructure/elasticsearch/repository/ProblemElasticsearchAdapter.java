package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemElasticsearchAdapter implements ProblemDocumentRepository {

	private final ProblemElasticsearchRepository searchRepository;

	public List<ProblemSearchDocument> findAllProblemByTitle(String title) {

		return searchRepository.findAllByTitleAndIsDeleted(title, false);
	}

	public ProblemSearchDocument save(ProblemSearchDocument problemSearch) {

		return searchRepository.save(problemSearch);
	}

	public Set<ProblemSearchDocument> findDocumentContainingKeyword(String keyword) {

		SearchHits<ProblemSearchDocument> hits = searchRepository.findFieldsContainingKeyword(keyword);

		return hits.getSearchHits().stream()
			.map(hit -> {

				List<String> descHighlights = hit.getHighlightField("description");

				String rawHighlight = !descHighlights.isEmpty()
					? descHighlights.get(0)
					: null;

				String highlightedDesc = null;
				if (rawHighlight != null) {
					highlightedDesc = rawHighlight
						.replaceAll("(?i)<em>", "")
						.replaceAll("(?i)</em>", "");
				}

				ProblemSearchDocument hitResult = hit.getContent();

				return ProblemSearchDocument.builder()
					.title(hitResult.getTitle())
					.category(hitResult.getCategory())
					.reference(hitResult.getReference())
					.difficulty(hitResult.getDifficulty())
					.description(highlightedDesc)
					.build();
			})
			.collect(Collectors.toSet());
	}

	public List<ProblemSearchDocument> findAllProblemByKeyword(String keyword) {

		return searchRepository.findAllByKeyword(keyword);
	}

	public Optional<ProblemSearchDocument> findById(Long id) {

		return searchRepository.findById(id);
	}

	public void delete(ProblemSearchDocument document) {

		document.softDelete();

		save(document);
	}
}
