package org.ezcode.codetest.application.notification.event.mapper;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.dto.event.ReplyCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.ReplyCreatePayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class ReplyCreateNotificationMapper implements NotificationMapper<ReplyCreateEvent> {

	@Override
	public Class<ReplyCreateEvent> getSupportedType() {
		return ReplyCreateEvent.class;
	}

	@Override
	public NotificationCreateEvent map(ReplyCreateEvent event) {

		return new NotificationCreateEvent(
			event.principalName(),
			NotificationType.COMMUNITY_REPLY,
			"새로운 댓글이 달렸습니다.",
			new ReplyCreatePayload(event.replyId(), event.discussionId(), event.content()),
			"/redirect",
			false,
			LocalDateTime.now()
		);
	}
}
