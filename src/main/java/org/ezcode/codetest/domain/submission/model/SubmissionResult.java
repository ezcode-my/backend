package org.ezcode.codetest.domain.submission.model;

import java.util.List;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;

import lombok.Builder;

@Builder
public record SubmissionResult(

	Long userId,

	List<String> problemCategory,

	boolean isSolved,

	boolean hasBeenSolved

) {
	public static SubmissionResult from(UserProblemResult result, boolean hasBeenSolved) {
		return SubmissionResult.builder()
			.userId(result.getUser().getId())
			.problemCategory(result.getProblemCategoryDescription())
			.isSolved(result.isCorrect())
			.hasBeenSolved(hasBeenSolved)
			.build();
	}
}
