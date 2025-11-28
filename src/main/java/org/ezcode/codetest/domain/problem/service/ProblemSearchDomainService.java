package org.ezcode.codetest.domain.problem.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.repository.ProblemSearchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchDomainService {

	// private final ProblemDocumentRepository searchRepository;
	private final ProblemSearchRepository searchRepository;

	public List<Problem> searchByKeywordMatch(String keyword) {

		return searchRepository.searchProblems(keyword);
	}
}
