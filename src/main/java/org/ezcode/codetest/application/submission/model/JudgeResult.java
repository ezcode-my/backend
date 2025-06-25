package org.ezcode.codetest.application.submission.model;

import lombok.Builder;

@Builder
public record JudgeResult(

    String actualOutput,

    long executionTime,

    long memoryUsage,

    boolean success,

    String message

) {
}
