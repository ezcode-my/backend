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
	public static SubmissionResult of(UserProblemResult upr, List<String> problemCategory, boolean allPassed) {
		boolean before = upr.isCorrect();
		boolean now = allPassed && !before;

		return SubmissionResult.builder()
			.userId(upr.getUser().getId())
			.problemCategory(problemCategory)
			.isSolved(now)
			.hasBeenSolved(before)
			.build();
	}
}
