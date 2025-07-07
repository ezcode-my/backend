package org.ezcode.codetest.application.submission.dto.event.payload;

import org.ezcode.codetest.application.submission.model.JudgeResult;

public record TestcaseResultPayload(

    Long testcaseId,

    boolean isPassed,

    String actualOutput,

    long executionTime,

    long memoryUsage,

    String message

) {
    public static TestcaseResultPayload fromEvaluation(Long testcaseId, boolean isPassed, JudgeResult result) {
        return new TestcaseResultPayload(
            testcaseId,
            isPassed,
            result.actualOutput(),
            result.executionTime(),
            result.memoryUsage(),
            result.message());
    }
}
