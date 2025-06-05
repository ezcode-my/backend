package org.ezcode.codetest.application.submission.model;

import lombok.Builder;

@Builder
public record JudgeResult(

	String actualOutput,

	String executionTime,

	Long memoryUsage,

	boolean success,

	String message

) {
}
