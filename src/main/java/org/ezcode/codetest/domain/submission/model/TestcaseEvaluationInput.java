package org.ezcode.codetest.domain.submission.model;

import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record TestcaseEvaluationInput(

    String expectedOutput,

    String actualOutput,

    String resultMessage,

    boolean success,

    long executionTime,

    long memoryUsage

) {
    public static TestcaseEvaluationInput from(Testcase testcase, JudgeResult result) {
        return new TestcaseEvaluationInput(
            testcase.getOutput(),
            result.actualOutput(),
            result.message(),
            result.success(),
            result.executionTime(),
            result.memoryUsage()
        );
    }
}
