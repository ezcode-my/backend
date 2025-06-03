package org.ezcode.codetest.domain.problem.service;

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
}
