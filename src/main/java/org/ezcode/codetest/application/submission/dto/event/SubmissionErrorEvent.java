package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;

public record SubmissionErrorEvent(

    String sessionKey,

    SubmissionExceptionCode code

) {
    public SubmissionErrorEvent(String sessionKey, Throwable t) {
        this(sessionKey, resolveCode(t));
    }

    private static SubmissionExceptionCode  resolveCode(Throwable t) {
        if (t instanceof SubmissionException se) {
            return (SubmissionExceptionCode) se.getResponseCode();
        }
        return SubmissionExceptionCode.UNKNOWN_ERROR;
    }
}
