package org.ezcode.codetest.application.submission.model;

import lombok.Builder;

@Builder
public record JudgeResult(

	String actualOutput,

	double executionTime,

	long memoryUsage,

	boolean success,

	String message

) {
}
