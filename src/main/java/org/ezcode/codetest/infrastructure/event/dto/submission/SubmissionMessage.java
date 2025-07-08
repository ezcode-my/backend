package org.ezcode.codetest.infrastructure.event.dto.submission;

import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;

public record SubmissionMessage(

    String sessionKey,

    Long problemId,

    Long languageId,

    Long userId,

    String sourceCode

) {
    public static SubmissionMessage of(CodeSubmitRequest request, Long problemId, Long userId) {
        return new SubmissionMessage(
            request.sessionKey(),
            problemId,
            request.languageId(),
            userId,
            request.sourceCode()
        );
    }
}
