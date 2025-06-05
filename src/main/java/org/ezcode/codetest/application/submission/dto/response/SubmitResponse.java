package org.ezcode.codetest.application.submission.dto.response;

import org.ezcode.codetest.domain.problem.model.dto.SubmitProcessResult;

import lombok.Builder;

@Builder
public record SubmitResponse(

	boolean isCorrect,

	String expectedOutput,

	String actualOutput,

	String executionTime,

	Long memoryUsage,

	String message

) {
	public static SubmitResponse from(SubmitProcessResult submitProcessResult) {
		return SubmitResponse.builder()
			.isCorrect(submitProcessResult.isCorrect())
			.expectedOutput(submitProcessResult.expectedOutput())
			.actualOutput(submitProcessResult.actualOutput())
			.executionTime(submitProcessResult.executionTime())
			.memoryUsage(submitProcessResult.memoryUsage())
			.message(submitProcessResult.message())
			.build();
	}
}
