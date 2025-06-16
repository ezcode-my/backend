package org.ezcode.codetest.application.notification.event.payload;

public record ReplyCreatePayload(
	Long problemId,
	Long replyId,
	Long discussionId,
	String content
) implements NotificationPayload {
}
