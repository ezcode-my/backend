package org.ezcode.codetest.application.problem.service;

import org.ezcode.codetest.application.problem.dto.request.TestcaseCreateRequest;
import org.ezcode.codetest.application.problem.dto.response.TestcaseResponse;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.problem.service.TestcaseDomainService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestcaseService {

	private final ProblemDomainService problemDomainService;
	private final TestcaseDomainService testcaseDomainService;

	public TestcaseResponse createTestcase(Long problemId, TestcaseCreateRequest request) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		Testcase savedTestcase = testcaseDomainService.createTestcase(
			TestcaseCreateRequest.toEntity(findProblem, request)
		);

		return TestcaseResponse.from(savedTestcase);
	}
}
