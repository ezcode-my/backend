package org.ezcode.codetest.infrastructure.event.dto.submission;

public record SubmissionMessage(

    String sessionKey,

    Long problemId,

    Long languageId,

    Long userId,

    String sourceCode

) {
}
