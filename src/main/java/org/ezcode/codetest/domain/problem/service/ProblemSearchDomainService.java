package org.ezcode.codetest.domain.problem.service;

import java.util.List;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

// ELASTICSEARCH 도메인서비스입니다. 요거는 상황보고 다른 도메인 서비스하고 합쳐도 될 것 같습니다.
// 일단 충돌날까봐 따로 빼두었습니다.

@Service
@RequiredArgsConstructor
public class ProblemSearchDomainService {

	private final ProblemDocumentRepository searchRepository;

	public Set<ProblemSearchDocument> getSuggestionsByKeyword(String keyword) {

		return searchRepository.findDocumentContainingKeyword(keyword);
	}

	public List<ProblemSearchDocument> getProblemByKeyword(String keyword) {

		return searchRepository.findAllProblemByKeyword(keyword);
	}
}
