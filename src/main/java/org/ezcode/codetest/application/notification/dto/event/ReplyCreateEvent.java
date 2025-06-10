package org.ezcode.codetest.application.notification.dto.event;

public record ReplyCreateEvent(
	String principalName,
	Long replyId,
	Long discussionId,
	String content
) {
}
