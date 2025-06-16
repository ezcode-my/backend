package org.ezcode.codetest.application.notification.event.payload;

public record DiscussionVotePayload(
	Long problemId,
	Long discussionId,
	String voterNickname
) implements NotificationPayload {
}
