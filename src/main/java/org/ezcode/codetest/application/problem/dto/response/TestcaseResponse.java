package org.ezcode.codetest.application.problem.dto.response;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

import lombok.Builder;

@Builder
public record TestcaseResponse(

	Long id,

	Problem problem,

	String input,

	String output
) {

	public static TestcaseResponse from(Testcase testcase) {

		return TestcaseResponse.builder()
			.id(testcase.getId())
			.problem(testcase.getProblem())
			.input(testcase.getInput())
			.output(testcase.getOutput())
			.build();
	}
}
