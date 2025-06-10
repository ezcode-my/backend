package org.ezcode.codetest.application.notification.dto.event;

public record DiscussionVoteEvent(
	String principalName,
	Long discussionId,
	String voterNickname
) {
}
