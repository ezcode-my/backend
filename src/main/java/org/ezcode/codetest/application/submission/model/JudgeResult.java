package org.ezcode.codetest.application.submission.model;

import lombok.Builder;

@Builder
public record JudgeResult(

	String output,

	String time,

	Long memory,

	boolean success,

	String message

) {
}
