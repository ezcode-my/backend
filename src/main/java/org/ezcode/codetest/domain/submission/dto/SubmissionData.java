package org.ezcode.codetest.domain.submission.dto;

import java.util.List;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.user.model.entity.User;

import lombok.Builder;

@Builder
public record SubmissionData(

    User user,

    Problem problem,

    Language language,

    List<Testcase> testCaseList,

    String code,

    String message,

    long executionTime,

    long memoryUsage

) {
    public static SubmissionData from(SubmissionContext ctx) {
        return SubmissionData.builder()
            .user(ctx.user())
            .problem(ctx.getProblem())
            .language(ctx.language())
            .testCaseList(ctx.getTestcases())
            .code(ctx.getSourceCode())
            .message(ctx.getCurrentMessage())
            .build();
    }

    public static Submission toEntity(SubmissionData submissionData, int testCasePassedCount) {
        return Submission.builder()
            .user(submissionData.user)
            .problem(submissionData.problem)
            .language(submissionData.language)
            .code(submissionData.code)
            .message(submissionData.message)
            .testCasePassedCount(testCasePassedCount)
            .testCaseTotalCount(submissionData.getTestCaseSize())
            .executionTime(submissionData.executionTime)
            .memoryUsage(submissionData.memoryUsage)
            .build();
    }

    public static Submission toEntity(SubmissionContext ctx) {
        return Submission.builder()
            .user(ctx.user())
            .problem(ctx.getProblem())
            .language(ctx.language())
            .code(ctx.getSourceCode())
            .message(ctx.getCurrentMessage())
            .testCasePassedCount(ctx.getPassedCount())
            .testCaseTotalCount(ctx.getTestcaseCount())
            .executionTime(ctx.aggregator().averageExecutionTime())
            .memoryUsage(ctx.aggregator().averageMemoryUsage())
            .build();
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public Long getProblemId() {
        return this.problem.getId();
    }

    public int getTestCaseSize() {
        return this.testCaseList.size();
    }
}
