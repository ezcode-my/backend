package org.ezcode.codetest.infrastructure.persistence.repository.problem;

import java.util.List;

import org.ezcode.codetest.domain.problem.exception.TestcaseException;
import org.ezcode.codetest.domain.problem.exception.code.TestcaseExceptionCode;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
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

	@Override
	public List<Testcase> findAllByProblem(Problem problem) {
		return testcaseJpaRepository.findAllByProblem(problem);
	}

	@Override
	public List<Testcase> findAllByProblemId(Long problemId) {
		return testcaseJpaRepository.findAllByProblem_Id(problemId);
	}

	@Override
	public Testcase findByTestcase(Long testcaseId) {

		return testcaseJpaRepository.findById(testcaseId)
			.orElseThrow(() -> new TestcaseException(TestcaseExceptionCode.TESTCASE_NOT_FOUND));
	}

	@Override
	public void delete(Testcase testcase) {

		testcaseJpaRepository.delete(testcase);
	}

}
