package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record TestcaseCreateRequest(

	Problem problem,

	String input,

	String output

) {

	// dto -> entity 변환
	public static Testcase toEntity(TestcaseCreateRequest request) {

		return Testcase.builder()
			.problem(request.problem)
			.input(request.input)
			.output(request.output)
			.build();
	}
}
