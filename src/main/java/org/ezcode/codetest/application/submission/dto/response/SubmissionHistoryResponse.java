package org.ezcode.codetest.application.submission.dto.response;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.problem.model.entity.Submission;

import lombok.Builder;

@Builder
public record SubmissionHistoryResponse(

	Long id,

	String sourceCode,

	boolean isCorrect,

	String message,

	String executionTime,

	Long memoryUsage,

	LocalDateTime submittedAt

) {
	public static SubmissionHistoryResponse from(Submission submission) {
		return SubmissionHistoryResponse.builder()
			.id(submission.getId())
			.sourceCode(submission.getCode())
			.isCorrect(submission.getTestCaseTotalCount() == submission.getTestCasePassedCount())
			.message(submission.getMessage())
			.executionTime(submission.getExecutionTime())
			.memoryUsage(submission.getMemoryUsage())
			.submittedAt(submission.getCreatedAt().withNano(0))
			.build();
	}
}
