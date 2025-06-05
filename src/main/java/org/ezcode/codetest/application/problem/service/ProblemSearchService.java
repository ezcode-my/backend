package org.ezcode.codetest.application.problem.service;

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

		Set<ProblemSearchDocument> result = searchDomainService.getSuggestionsByKeyword(keyword);

		return result.stream()
			.flatMap(doc -> Stream.of(
				Optional.ofNullable(doc.getTitle()),
				Optional.ofNullable(doc.getReference()).map(Enum::toString),
				Optional.ofNullable(doc.getDifficulty()),
				Optional.ofNullable(doc.getCategory()).map(Enum::toString),
				Optional.ofNullable(doc.getDescription())
			))
			.flatMap(Optional::stream)
			.map(String::toUpperCase)
			.collect(Collectors.toSet());

	}

	public List<ProblemSearchResponse> getProblemSearchResult(String keyword) {

		List<ProblemSearchDocument> results = searchDomainService.searchByKeywordMatch(keyword);

		return results.stream().map(ProblemSearchResponse::from).toList();
	}
}
