package org.ezcode.codetest.application.problem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearch;
import org.ezcode.codetest.domain.problem.service.SearchDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchService {

	private final SearchDomainService searchDomainService;

	@Transactional
	public List<String> getProblemSuggestions(String keyword) {

		List<ProblemSearch> result = searchDomainService.getProblemByTitle(keyword);

		return result.stream().map(ProblemSearch::getTitle).toList();
	}

	@Transactional
	public List<Long> getProblemSearchResult(String keyword) {

		List<ProblemSearch> titleResult = searchDomainService.getProblemByTitle(keyword);

		List<ProblemSearch> descriptionResult = searchDomainService.getProblemByDescription(keyword);

		Map<Long, ProblemSearch> dedup = Stream.concat(
			titleResult.stream(),
			descriptionResult.stream()
		).collect(Collectors.toMap(
			ProblemSearch::getId,
			Function.identity(),
			(a, b) -> a
		));
		List<ProblemSearch> combined = new ArrayList<>(dedup.values());

		return combined.stream().map(ProblemSearch::getId).toList();
	}
}
