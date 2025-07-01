package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.application.submission.dto.event.payload.SubmissionFinalResultPayload;
import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record SubmissionJudgingFinishedEvent(

    String sessionKey,

    String principalName,

    SubmissionFinalResultPayload payload

) {
    public static SubmissionJudgingFinishedEvent from(SubmissionContext ctx) {
        return new SubmissionJudgingFinishedEvent(
            ctx.getSessionKey(),
            ctx.getUserEmail(),
            ctx.toFinalResult()
        );
    }
}
