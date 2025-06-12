package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

import io.swagger.v3.oas.annotations.media.Schema;

public record TestcaseCreateRequest(

	@Schema(description = "입력값", example = "2 3")
	String input,

	@Schema(description = "기댓값", example = "5")
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
