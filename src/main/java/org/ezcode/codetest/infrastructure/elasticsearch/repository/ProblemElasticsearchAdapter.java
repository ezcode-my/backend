package org.ezcode.codetest.infrastructure.elasticsearch.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProblemElasticsearchAdapter implements ProblemDocumentRepository {

	private final ProblemElasticsearchRepository searchRepository;

	public ProblemSearchDocument save(ProblemSearchDocument problemSearch) {

		return searchRepository.save(problemSearch);
	}

	private String getElement(List<String> list) {
		return (list != null && !list.isEmpty()) ? list.get(0) : null;
	}

	private List<String> getElementList(List<String> list) {
		return (list != null && !list.isEmpty()) ? list : null;
	}

	public Set<ProblemSearchDocument> findDocumentContainingKeyword(String keyword) {

		SearchHits<ProblemSearchDocument> hits = searchRepository.findFieldsContainingKeyword(keyword);

		return hits.getSearchHits().stream()
			.map(hit -> {
				Map<String, List<String>> hitHighlightFields = hit.getHighlightFields();

				String titleStr = getElement(hitHighlightFields.get("title"));
				List<String> categoryStr = getElementList(hitHighlightFields.get("categories"));
				String referenceStr = getElement(hitHighlightFields.get("reference"));
				String difficulty = getElement(hitHighlightFields.get("difficulty"));
				String descHighlight = getElement(hitHighlightFields.get("description"));
				List<String> categoryKorStr = getElementList(hitHighlightFields.get("categoriesKor"));
				String referenceKorStr = getElement(hitHighlightFields.get("referenceKor"));
				String difficultyEn = getElement(hitHighlightFields.get("difficultyEn"));

				ProblemSearchDocument.ProblemSearchDocumentBuilder builder = ProblemSearchDocument.builder()
					.title(titleStr)
					.categories(categoryStr != null ? categoryStr.stream().map(Category::valueOf).toList() : null)
					.reference(referenceStr != null ? Reference.valueOf(referenceStr) : null)
					.difficulty(difficulty)
					.categoriesKor(categoryKorStr)
					.referenceKor(referenceKorStr)
					.difficultyEn(difficultyEn != null ? Difficulty.valueOf(difficultyEn) : null);

				if (descHighlight != null) {
					builder.description(keyword);
				}

				return builder.build();
			})
			.collect(Collectors.toSet());
	}

	@Deprecated
	public List<ProblemSearchDocument> findAllByKeyword(String keyword) {

		return searchRepository.findAllByKeyword(keyword);
	}

	public List<ProblemSearchDocument> findProblemsByKeyword(String keyword) {

		SearchHits<ProblemSearchDocument> hits = searchRepository.findProblemsByKeyword(keyword);

		return hits.getSearchHits().stream().map(SearchHit::getContent).toList();
	}

	public Optional<ProblemSearchDocument> findById(Long id) {

		return searchRepository.findById(id);
	}

	public void delete(ProblemSearchDocument document) {

		document.softDelete();

		save(document);
	}
}
