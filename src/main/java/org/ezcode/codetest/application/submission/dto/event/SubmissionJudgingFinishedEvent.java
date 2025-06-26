package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.application.submission.dto.event.payload.SubmissionFinalResultPayload;

public record SubmissionJudgingFinishedEvent(

    String sessionKey,

    SubmissionFinalResultPayload payload

) {
}
