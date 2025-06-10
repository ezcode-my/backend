package org.ezcode.codetest.application.notification.event.mapper;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.dto.event.DiscussionVoteEvent;
import org.ezcode.codetest.application.notification.event.payload.DiscussionVotePayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class DiscussionVoteNotificationMapper implements NotificationMapper<DiscussionVoteEvent> {

	@Override
	public Class<DiscussionVoteEvent> getSupportedType() {
		return DiscussionVoteEvent.class;
	}

	@Override
	public NotificationCreateEvent map(DiscussionVoteEvent event) {

		return new NotificationCreateEvent(
			event.principalName(),
			NotificationType.COMMUNITY_DISCUSSION_VOTED_UP,
			"자유글에 추천을 받았습니다.",
			new DiscussionVotePayload(event.discussionId(), event.voterNickname()),
			"/redirect",
			false,
			LocalDateTime.now()
		);
	}
}
