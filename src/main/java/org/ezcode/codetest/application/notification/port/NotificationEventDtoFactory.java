package org.ezcode.codetest.application.notification.port;

import java.time.LocalDateTime;
import java.util.Map;

import org.ezcode.codetest.application.notification.dto.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.enums.NotificationType;

public class NotificationEventDtoFactory {

	public static NotificationCreateEvent forReplyCreated(
		String principalName,
		Long replyId,
		Long discussionId,
		String content
	) {

		return NotificationCreateEvent
			.builder()

			.principalName(principalName)
			.notificationType(NotificationType.COMMUNITY_REPLY)
			.message("새로운 댓글이 달렸습니다.")
			.payload(Map.of(
				"replyId", replyId,
				"discussionId", discussionId,
				"content", content
			))
			.redirectUrl("/redirect")
			.isRead(false)
			.createdAt(LocalDateTime.now())

			.build();
	}

	public static NotificationCreateEvent forDiscussionVoteCreated(
		String principalName,
		Long discussionId,
		String voter
	) {

		return NotificationCreateEvent
			.builder()

			.principalName(principalName)
			.notificationType(NotificationType.COMMUNITY_DISCUSSION_VOTED_UP)
			.message("자유글에 추천을 받았습니다.")
			.payload(Map.of(
				"discussionId", discussionId,
				"voter", voter
			))
			.redirectUrl("/redirect")
			.isRead(false)
			.createdAt(LocalDateTime.now())

			.build();
	}

	public static NotificationCreateEvent forReplyVoteCreated(
		String principalName,
		Long replyId,
		String voter
	) {

		return NotificationCreateEvent
			.builder()

			.principalName(principalName)
			.notificationType(NotificationType.COMMUNITY_REPLY_VOTED_UP)
			.message("댓글에 추천을 받았습니다.")
			.payload(Map.of(
				"replyId", replyId,
				"voter", voter
			))
			.redirectUrl("/redirect")
			.isRead(false)
			.createdAt(LocalDateTime.now())

			.build();
	}

}
