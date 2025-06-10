package org.ezcode.codetest.application.notification.dto.payload;

public record DiscussionVotePayload(
	Long discussionId,
	String voterNickname
) implements NotificationPayload {
}
