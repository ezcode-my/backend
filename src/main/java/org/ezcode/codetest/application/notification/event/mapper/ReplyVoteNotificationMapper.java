package org.ezcode.codetest.application.notification.event.mapper;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.dto.event.ReplyVoteEvent;
import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.payload.ReplyVotePayload;
import org.springframework.stereotype.Component;

@Component
public class ReplyVoteNotificationMapper implements NotificationMapper<ReplyVoteEvent> {

	@Override
	public Class<ReplyVoteEvent> getSupportedType() {
		return ReplyVoteEvent.class;
	}

	@Override
	public NotificationCreateEvent map(ReplyVoteEvent event) {

		return new NotificationCreateEvent(
			event.principalName(),
			NotificationType.COMMUNITY_REPLY_VOTED_UP,
			"댓글에 추천을 받았습니다.",
			new ReplyVotePayload(event.replyId(), event.voterNickname()),
			"/redirect",
			false,
			LocalDateTime.now()
		);
	}
}
