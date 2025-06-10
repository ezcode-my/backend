package org.ezcode.codetest.application.notification.dto.payload;

public record ReplyCreatePayload(
	Long replyId,
	Long discussionId,
	String content
) implements NotificationPayload {
}
