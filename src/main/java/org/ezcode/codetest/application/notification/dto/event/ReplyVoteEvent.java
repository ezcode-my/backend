package org.ezcode.codetest.application.notification.dto.event;

public record ReplyVoteEvent(
	String principalName,
	Long replyId,
	String voterNickname
) {
}
