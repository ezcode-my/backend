package org.ezcode.codetest.domain.problem.service;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearch;
import org.ezcode.codetest.domain.problem.repository.SearchRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SearchDomainService {

	private final SearchRepository searchRepository;

	public List<ProblemSearch> getProblemByTitle(String keyword) {

		return searchRepository.findAllProblemByTitle(keyword);
	}

	public List<ProblemSearch> getProblemByDescription(String keyword) {

		return searchRepository.findAllProblemByDescription(keyword);
	}
}
