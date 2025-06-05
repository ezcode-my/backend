package org.ezcode.codetest.domain.problem.model.dto;

import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.Builder;

@Builder
public record SubmissionData(

	User user,

	Problem problem,

	Language language,

	String code,

	String message,

	String executionTime,

	Long memoryUsage

) {
	public static SubmissionData of(
		User user, Problem problem, Language language, String code,
		String message, String executionTime, Long memoryUsage) {
		return SubmissionData.builder()
			.user(user)
			.problem(problem)
			.language(language)
			.code(code)
			.message(message)
			.executionTime(executionTime)
			.memoryUsage(memoryUsage)
			.build();
	}

	public static Submission toEntity(SubmissionData submissionData, int testCasePassedCount) {
		return Submission.builder()
			.user(submissionData.user)
			.problem(submissionData.problem())
			.language(submissionData.language())
			.code(submissionData.code())
			.message(submissionData.message())
			.testCasePassedCount(testCasePassedCount)
			.testCaseTotalCount(1)
			.executionTime(submissionData.executionTime())
			.memoryUsage(submissionData.memoryUsage())
			.build();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public Long getProblemId() {
		return this.problem.getId();
	}
}
