package org.ezcode.codetest.application.problem.service;

import java.util.List;
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

		return result.stream().flatMap(document -> Stream.of(
			document.getTitle(),
			document.getDescription(),
			document.getReference().toString(),
			document.getDifficulty(),
			document.getCategory().toString()
		)).filter(value -> {
			if (value == null || value.isEmpty()) return false;
			return value.toLowerCase().contains(keyword);
		}).collect(Collectors.toSet());
	}

	public List<ProblemSearchResponse> getProblemSearchResult(String keyword) {

		List<ProblemSearchDocument> result = searchDomainService.getProblemByKeyword(keyword);

		return result.stream().map(ProblemSearchResponse::from).toList();
	}
}
