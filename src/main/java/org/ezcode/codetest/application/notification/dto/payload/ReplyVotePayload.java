package org.ezcode.codetest.application.notification.dto.payload;

public record ReplyVotePayload(
	Long replyId,
	String voterNickname
) implements NotificationPayload {
}
