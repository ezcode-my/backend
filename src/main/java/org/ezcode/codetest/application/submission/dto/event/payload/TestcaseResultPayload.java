package org.ezcode.codetest.application.submission.dto.event.payload;

import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;

public record TestcaseResultPayload(

    int seqId,

    boolean isPassed,

    String actualOutput,

    long executionTime,

    long memoryUsage,

    String message

) {
    public static TestcaseResultPayload fromEvaluation(int seqId, JudgeResult result, AnswerEvaluation evaluation) {
        return new TestcaseResultPayload(
            seqId,
            evaluation.isPassed(),
            result.actualOutput(),
            result.executionTime(),
            result.memoryUsage(),
            result.message());
    }
}
