package org.ezcode.codetest.application.submission.dto.response.submission;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.submission.model.entity.Submission;

import lombok.Builder;

@Builder
public record SubmissionDetailResponse(

	Long id,

	String sourceCode,

	boolean isCorrect,

	String message,

	Double executionTime,

	Long memoryUsage,

	LocalDateTime submittedAt

) {
	public static SubmissionDetailResponse from(Submission submission) {
		return new SubmissionDetailResponse(
			submission.getId(),
			submission.getCode(),
			submission.isCorrect(),
			submission.getMessage(),
			submission.getExecutionTime(),
			submission.getMemoryUsage(),
			submission.getCreatedAt().withNano(0)
		);
	}
}
