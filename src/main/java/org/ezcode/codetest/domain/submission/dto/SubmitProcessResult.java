package org.ezcode.codetest.domain.submission.dto;

import lombok.Builder;

@Builder
public record SubmitProcessResult(

    boolean isCorrect,

    String expectedOutput,

    String actualOutput,

    double executionTime,

    long memoryUsage,

    String message

) {
    public static SubmitProcessResult of(SubmissionData submissionData, AnswerEvaluation evaluation) {
        return SubmitProcessResult.builder()
            .isCorrect(evaluation.isCorrect())
            .expectedOutput(evaluation.expectedOutput())
            .actualOutput(evaluation.actualOutput())
            .executionTime(submissionData.executionTime())
            .memoryUsage(submissionData.memoryUsage())
            .message(submissionData.message())
            .build();
    }
}
