package org.ezcode.codetest.application.submission.dto.response.submission;

import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;

import lombok.Builder;

@Builder
public record JudgeResultResponse(

	boolean isPassed,

	String expectedOutput,

	String actualOutput,

	Double executionTime,

	Long memoryUsage,

	String message

) {
	public static JudgeResultResponse fromEvaluation(JudgeResult result, AnswerEvaluation evaluation) {
		return new JudgeResultResponse(
			evaluation.isPassed(),
			evaluation.expectedOutput(),
			evaluation.actualOutput(),
			result.executionTime(),
			result.memoryUsage(),
			result.message());
	}
}
