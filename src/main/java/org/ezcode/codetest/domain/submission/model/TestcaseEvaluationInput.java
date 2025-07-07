package org.ezcode.codetest.domain.submission.model;

import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record TestcaseEvaluationInput(

    String expectedOutput,

    String actualOutput,

    String resultMessage,

    boolean success,

    long executionTime,

    long memoryUsage,

    long timeLimit,

    long memoryLimit

) {
    public static TestcaseEvaluationInput from(JudgeResult result, SubmissionContext ctx, int index) {
        return new TestcaseEvaluationInput(
            ctx.getExpectedOutput(index),
            result.actualOutput(),
            result.message(),
            result.success(),
            result.executionTime(),
            result.memoryUsage(),
            ctx.getTimeLimit(),
            ctx.getMemoryLimit()
        );
    }

    public boolean isCorrect() {
        return success && expectedOutput.strip().equals(actualOutput.strip());
    }

    public boolean timeEfficient() {
        return executionTime <= timeLimit;
    }

    public boolean memoryEfficient() {
        return memoryUsage <= memoryLimit;
    }
}
