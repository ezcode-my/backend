package org.ezcode.codetest.application.problem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.service.ProblemSearchDomainService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchService {

	private final ProblemSearchDomainService searchDomainService;

	//검색 자동완성 기능입니다. 프론트에서 글자를 입력할 때마다 ES 에서 조회해서 반환해줍니다.
	public Set<String> getProblemSuggestions(String keyword) {

		List<ProblemSearchDocument> result = searchDomainService.getProblemByTitle(keyword);

		return result.stream().map(ProblemSearchDocument::getTitle).collect(Collectors.toSet());
	}

	//프론트에서 폼에 전송 버튼 눌렀을 시 해당 검색어로 description, title 검색알고리즘을 통해
	//사용자한테 Problem PK 목록을 반환합니다.
	public List<Long> getProblemSearchResult(String keyword) {

		List<ProblemSearchDocument> titleResult = searchDomainService.getProblemByTitle(keyword);

		List<ProblemSearchDocument> descriptionResult = searchDomainService.getProblemByDescription(keyword);

		Map<Long, ProblemSearchDocument> dedupe = Stream.concat(
			titleResult.stream(),
			descriptionResult.stream()
		).collect(Collectors.toMap(
			ProblemSearchDocument::getId,
			Function.identity(),
			(a, b) -> a)
		);

		List<ProblemSearchDocument> combined = new ArrayList<>(dedupe.values());

		return combined.stream().map(ProblemSearchDocument::getId).toList();
	}
}
