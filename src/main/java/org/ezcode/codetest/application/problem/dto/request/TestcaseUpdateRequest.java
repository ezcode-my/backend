package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record TestcaseUpdateRequest(

	String input,

	String output
) {

	public static Testcase from (TestcaseUpdateRequest request, Problem problem) {

		return Testcase.builder()
			.problem(problem)
			.input(request.input)
			.output(request.output)
			.build();

	}
}
