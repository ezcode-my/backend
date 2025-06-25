package org.ezcode.codetest.domain.submission.dto;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
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
    public static SubmissionData base(
        User user, ProblemInfo problemInfo, Language language, String code, String message) {
        return SubmissionData.builder()
            .user(user)
            .problem(problemInfo.problem())
            .language(language)
            .testCaseList(problemInfo.testcaseList())
            .code(code)
            .message(message)
            .build();
    }

    public SubmissionData withAggregatedStats(SubmissionAggregator aggregator) {
        return SubmissionData.builder()
            .user(this.user)
            .problem(this.problem)
            .language(this.language)
            .testCaseList(this.testCaseList)
            .code(this.code)
            .message(this.message)
            .executionTime(aggregator.averageExecutionTime())
            .memoryUsage(aggregator.averageMemoryUsage())
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
