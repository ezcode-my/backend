package org.ezcode.codetest.application.submission.dto.event;

public record GitStatusEvent(

    String sessionKey,

    String message

) {
    public static GitStatusEvent onStart(String sessionKey) {
        return new GitStatusEvent(sessionKey, "시작");
    }

    public static GitStatusEvent onSuccess(String sessionKey) {
        return new GitStatusEvent(sessionKey, "성공");
    }

    public static GitStatusEvent onFailed(String sessionKey) {
        return new GitStatusEvent(sessionKey, "실패");
    }
}
