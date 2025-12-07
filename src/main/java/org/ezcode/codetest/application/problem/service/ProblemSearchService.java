package org.ezcode.codetest.application.problem.service;

import java.util.List;
import java.util.Set;

import org.ezcode.codetest.application.problem.dto.response.ProblemSearchResponse;
import org.ezcode.codetest.domain.problem.service.ProblemSearchDomainService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchService {

	private final ProblemSearchDomainService searchDomainService;

	// Redis 캐시 적용 (Cache Name: suggestions, Key: keyword)
	@Cacheable(value = "suggestions", key = "#keyword", unless = "#result.isEmpty()")
	public Set<String> getProblemSuggestions(String keyword) {

		return null;
	}

	public List<ProblemSearchResponse> getProblemSearchResult(String keyword) {

		return searchDomainService.searchByKeywordMatch(keyword)
			.stream()
			.map(ProblemSearchResponse::from)
			.toList();
	}
}
