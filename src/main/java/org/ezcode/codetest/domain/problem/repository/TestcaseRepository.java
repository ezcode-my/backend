package org.ezcode.codetest.domain.problem.repository;

import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public interface TestcaseRepository {

	Testcase save(Testcase testcase);
}
