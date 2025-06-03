package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.problem.repository.TestcaseRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TestcaseRepositoryImpl implements TestcaseRepository {

	private final TestcaseJpaRepository testcaseJpaRepository;

	@Override
	public Testcase save(Testcase testcase) {
		return testcaseJpaRepository.save(testcase);
	}
}
