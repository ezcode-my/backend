package org.ezcode.codetest.application.submission.dto.event;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.event.payload.InitTestcaseListPayload;
import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record TestcaseListInitializedEvent(

    String sessionKey,

    String principalName,

    List<InitTestcaseListPayload> payload

) {
    public static TestcaseListInitializedEvent of(SubmissionContext ctx, List<InitTestcaseListPayload> payload) {
        return new TestcaseListInitializedEvent(ctx.getSessionKey(), ctx.getUserEmail(), payload);
    }
}
