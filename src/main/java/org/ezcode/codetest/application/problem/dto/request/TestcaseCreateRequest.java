package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record TestcaseCreateRequest(

	String input,

	String output

) {

	// dto -> entity 변환
	public static Testcase toEntity(Problem problem, TestcaseCreateRequest request) {

		return Testcase.builder()
			.problem(problem)
			.input(request.input)
			.output(request.output)
			.build();
	}
}
