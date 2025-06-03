package org.ezcode.codetest.application.submission.dto.response;

import lombok.Builder;

@Builder
public record SubmitResponse(

	boolean isCorrect,

	String expectedOutput,

	String actualOutput,

	String time,

	Long memory,

	String message

) {
}
