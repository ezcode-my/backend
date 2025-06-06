package org.ezcode.codetest.domain.problem.service;

import java.util.List;
import java.util.Set;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;
import org.ezcode.codetest.domain.problem.repository.ProblemDocumentRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSearchDomainService {

	private final ProblemDocumentRepository searchRepository;

	public Set<ProblemSearchDocument> getSuggestionsByKeyword(String keyword) {

		return searchRepository.findDocumentContainingKeyword(keyword);
	}

	public List<ProblemSearchDocument> searchByKeywordMatch(String keyword) {

		return searchRepository.findAllByKeyword(keyword);
	}
}
