package org.ezcode.codetest.application.problem.service;

import java.util.List;

import org.ezcode.codetest.application.problem.dto.request.TestcaseCreateRequest;
import org.ezcode.codetest.application.problem.dto.request.TestcaseUpdateRequest;
import org.ezcode.codetest.application.problem.dto.response.TestcaseResponse;
import org.ezcode.codetest.domain.problem.exception.TestcaseException;
import org.ezcode.codetest.domain.problem.exception.code.TestcaseExceptionCode;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.problem.service.TestcaseDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestcaseService {

	private final ProblemDomainService problemDomainService;
	private final TestcaseDomainService testcaseDomainService;

	@Transactional
	public TestcaseResponse createTestcase(Long problemId, TestcaseCreateRequest request) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		Testcase savedTestcase = testcaseDomainService.createTestcase(
			TestcaseCreateRequest.toEntity(findProblem, request)
		);

		return TestcaseResponse.from(savedTestcase);
	}

	@Transactional(readOnly = true)
	public List<TestcaseResponse> getTestcaseList(Long problemId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		List<Testcase> testcaseList = testcaseDomainService.getTestcaseList(findProblem);

		return testcaseList
				.stream()
				.map(TestcaseResponse::from)
				.toList();

	}

	@Transactional
	public TestcaseResponse modifyTestcase(Long problemId, Long testcaseId, TestcaseUpdateRequest request) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		Testcase findtestcase = testcaseDomainService.getTestcase(testcaseId);

		// 테스트케이스가 해당 문제에 속하는지 검증
		if(!findtestcase.problemIdMatched(findProblem.getId())) {
			throw new TestcaseException(TestcaseExceptionCode.TESTCASE_NOT_FOUND);
		}

		findtestcase.update(request.input(), request.output());

		return TestcaseResponse.from(findtestcase);
	}

	@Transactional
	public void removeTestcase(Long problemId, Long testcaseId) {

		Problem findProblem = problemDomainService.getProblem(problemId);

		testcaseDomainService.removeTestcase(findProblem, testcaseId);
	}
}
