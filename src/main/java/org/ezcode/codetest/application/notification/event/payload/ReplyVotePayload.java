package org.ezcode.codetest.application.notification.event.payload;

public record ReplyVotePayload(
	Long replyId,
	String voterNickname
) implements NotificationPayload {
}
