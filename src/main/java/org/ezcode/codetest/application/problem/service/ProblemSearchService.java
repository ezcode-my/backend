package org.ezcode.codetest.application.problem.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ezcode.codetest.application.problem.dto.response.ProblemSearchResponse;
import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.service.ProblemSearchDomainService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchService {

	private final ProblemSearchDomainService searchDomainService;

	public Set<String> getProblemSuggestions(String keyword) {

		return searchDomainService
			.getSuggestionsByKeyword(keyword)
			.stream()
			.flatMap(doc -> {
				Stream.Builder<String> searchedKeyword = Stream.builder();

				Optional.ofNullable(doc.getTitle()).ifPresent(searchedKeyword::add);
				Optional.ofNullable(doc.getReference())
					.map(Enum::toString)
					.ifPresent(searchedKeyword::add);
				Optional.ofNullable(doc.getDifficulty())
					.ifPresent(searchedKeyword::add);
				Optional.ofNullable(doc.getDescription())
					.ifPresent(searchedKeyword::add);
				Optional.ofNullable(doc.getDifficultyEn())
					.map(Enum::toString)
					.ifPresent(searchedKeyword::add);
				Optional.ofNullable(doc.getReferenceKor())
					.ifPresent(searchedKeyword::add);

				Optional.ofNullable(doc.getCategories())
					.stream()
					.flatMap(Collection::stream)
					.map(Enum::toString)
					.forEach(searchedKeyword::add);

				Optional.ofNullable(doc.getCategoriesKor())
					.stream()
					.flatMap(Collection::stream)
					.forEach(searchedKeyword::add);

				return searchedKeyword.build();
			})
			.map(String::toUpperCase)
			.collect(Collectors.toSet());
	}

	public List<ProblemSearchResponse> getProblemSearchResult(String keyword) {

		List<ProblemSearchDocument> results = searchDomainService.searchByKeywordMatch(keyword);

		return results.stream().map(ProblemSearchResponse::from).toList();
	}
}
