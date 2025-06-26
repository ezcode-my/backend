package org.ezcode.codetest.application.submission.dto.event.payload;

import org.ezcode.codetest.application.submission.model.JudgeResult;

public record TestcaseResultPayload(

    int seqId,

    boolean isPassed,

    String actualOutput,

    long executionTime,

    long memoryUsage,

    String message

) {
    public static TestcaseResultPayload fromEvaluation(int seqId, boolean isPassed, JudgeResult result) {
        return new TestcaseResultPayload(
            seqId,
            isPassed,
            result.actualOutput(),
            result.executionTime(),
            result.memoryUsage(),
            result.message());
    }
}
