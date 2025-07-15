package org.ezcode.codetest.domain.submission.model;

import java.util.List;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;

import lombok.Builder;

@Builder
public record SubmissionResult(

	Long userId,

	List<String> problemCategory,

	boolean isSolved,

	boolean hasBeenSolved

) {
	public static SubmissionResult of(
		UserProblemResult upr,
		SubmissionContext ctx,
		boolean allPassed,
		boolean before
	) {
		return SubmissionResult.builder()
			.userId(upr.getUser().getId())
			.problemCategory(ctx.getCategories())
			.isSolved(allPassed && !before)
			.hasBeenSolved(before)
			.build();
	}
}
