package org.ezcode.codetest.application.notification.event.payload;

public record ReplyCreatePayload(
	Long problemId,
	Long discussionId,
	Long replyId,
	Long parentReplyId,
	Long authorId,
	String authorNickname,
	String content
) implements NotificationPayload {
}
