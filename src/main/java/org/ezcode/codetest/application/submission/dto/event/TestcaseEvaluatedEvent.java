package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.application.submission.dto.event.payload.TestcaseResultPayload;
import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record TestcaseEvaluatedEvent(

    String sessionKey,

    String principalName,

    TestcaseResultPayload payload

) {
    public static TestcaseEvaluatedEvent of(SubmissionContext ctx, TestcaseResultPayload payload) {
        return new TestcaseEvaluatedEvent(
            ctx.getSessionKey(),
            ctx.getUserEmail(),
            payload
        );
    }
}
