package org.ezcode.codetest.domain.problem.repository;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public interface TestcaseRepository {

	Testcase save(Testcase testcase);

	List<Testcase> findAllByProblem(Problem problem);

	List<Testcase> findAllByProblemId(Long problemId);

	Testcase findByTestcase(Long testcaseId);

	void delete(Testcase testcase);
}
