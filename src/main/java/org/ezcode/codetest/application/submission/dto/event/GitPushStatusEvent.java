package org.ezcode.codetest.application.submission.dto.event;

import org.ezcode.codetest.infrastructure.github.model.PushStatus;

public record GitPushStatusEvent(

    String sessionKey,

    PushStatus pushStatus

) {
    public static GitPushStatusEvent started(String sessionKey) {
        return new GitPushStatusEvent(sessionKey, PushStatus.STARTED);
    }

    public static GitPushStatusEvent succeeded(String sessionKey) {
        return new GitPushStatusEvent(sessionKey, PushStatus.SUCCESS);
    }

    public static GitPushStatusEvent failed(String sessionKey) {
        return new GitPushStatusEvent(sessionKey, PushStatus.FAILED);
    }
}
