package org.ezcode.codetest.application.problem.dto.response;

import org.ezcode.codetest.domain.problem.model.entity.Testcase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TestcaseResponse(

	@Schema(description = "PK", example = "1")
	Long id,

	@Schema(description = "FK", example = "1")
	Long problemId,

	@Schema(description = "입력값", example = "2 3")
	String input,

	@Schema(description = "기댓값", example = "5")
	String output
) {

	public static TestcaseResponse from(Testcase testcase) {

		return TestcaseResponse.builder()
			.id(testcase.getId())
			.problemId(testcase.getProblem().getId())
			.input(testcase.getInput())
			.output(testcase.getOutput())
			.build();
	}
}
