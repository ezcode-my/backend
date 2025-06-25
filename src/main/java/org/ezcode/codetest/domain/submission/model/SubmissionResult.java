package org.ezcode.codetest.domain.submission.model;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;

import lombok.Builder;

@Builder
public record SubmissionResult(

	Long userId,

	String problemCategory,

	boolean isSolved,

	boolean hasBeenSolved

) {
	public static SubmissionResult from(UserProblemResult result, boolean hasBeenSolved) {
		return SubmissionResult.builder()
			.userId(result.getUser().getId())
			.problemCategory(result.getProblem().getCategory().getDescription())
			.isSolved(result.isCorrect())
			.hasBeenSolved(hasBeenSolved)
			.build();
	}
}
