package org.ezcode.codetest.infrastructure.event.dto;

public record SubmissionMessage(

	String emitterKey,

	Long problemId,

	Long languageId,

	Long userId,

	String sourceCode

) {
}
