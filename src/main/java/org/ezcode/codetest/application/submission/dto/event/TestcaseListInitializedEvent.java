package org.ezcode.codetest.application.submission.dto.event;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.event.payload.InitTestcaseListPayload;

public record TestcaseListInitializedEvent(

    String sessionKey,

    List<InitTestcaseListPayload> payload

) {
    public static TestcaseListInitializedEvent from(String sessionKey, List<InitTestcaseListPayload> payload) {
        return new TestcaseListInitializedEvent(sessionKey, payload);
    }
}
