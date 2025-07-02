package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.infrastructure.github.model.PushStatus;

public record GitPushStatusEvent(

    String sessionKey,

    String principalName,

    PushStatus pushStatus

) {
    public static GitPushStatusEvent started(SubmissionContext ctx) {
        return new GitPushStatusEvent(ctx.getSessionKey(), ctx.getUserEmail(), PushStatus.STARTED);
    }

    public static GitPushStatusEvent succeeded(SubmissionContext ctx) {
        return new GitPushStatusEvent(ctx.getSessionKey(), ctx.getUserEmail(), PushStatus.SUCCESS);
    }

    public static GitPushStatusEvent failed(SubmissionContext ctx) {
        return new GitPushStatusEvent(ctx.getSessionKey(), ctx.getUserEmail(), PushStatus.FAILED);
    }
}
