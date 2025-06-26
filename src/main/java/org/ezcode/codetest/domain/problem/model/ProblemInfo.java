package org.ezcode.codetest.domain.problem.model;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record ProblemInfo(

	Problem problem,

	List<Testcase> testcaseList

) {
	public long getTimeLimit() {
		return this.problem.getTimeLimit();
	}

	public long getMemoryLimit() {
		return this.problem.getMemoryLimit();
	}

	public int getTestcaseCount() {
		return this.testcaseList.size();
	}
}
