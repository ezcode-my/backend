package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.application.submission.dto.event.payload.TestcaseResultPayload;

public record TestcaseEvaluatedEvent(

    String sessionKey,

    TestcaseResultPayload payload

) {
    public static TestcaseEvaluatedEvent of(String sessionKey, TestcaseResultPayload payload) {
        return new TestcaseEvaluatedEvent(
            sessionKey,
            payload
        );
    }
}
