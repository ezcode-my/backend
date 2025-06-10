package org.ezcode.codetest.application.notification.event.payload;

public record DiscussionVotePayload(
	Long discussionId,
	String voterNickname
) implements NotificationPayload {
}
