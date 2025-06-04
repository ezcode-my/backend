package org.ezcode.codetest.domain.problem.service;

import java.util.List;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.problem.repository.TestcaseRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestcaseDomainService {

	private final TestcaseRepository testcaseRepository;

	public Testcase createTestcase(Testcase testcase) {

		return testcaseRepository.save(testcase);
	}

	public List<Testcase> getTestcaseList(Problem problem) {

		if (problem == null || problem.getIsDeleted()) {
			throw new ProblemException(ProblemExceptionCode.PROBLEM_NOT_FOUND);
		}

		return testcaseRepository.findAllByProblem(problem);
	}

	public Testcase getTestcase(Long testcaseId) {
		return testcaseRepository.findByTestcase(testcaseId);
	}
}
