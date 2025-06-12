package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

import io.swagger.v3.oas.annotations.media.Schema;

public record TestcaseUpdateRequest(

	@Schema(description = "입력값", example = "2 3")
	String input,

	@Schema(description = "기댓값", example = "5")
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
