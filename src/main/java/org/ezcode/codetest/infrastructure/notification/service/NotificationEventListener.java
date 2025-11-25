package org.ezcode.codetest.infrastructure.notification.service;

import org.ezcode.codetest.infrastructure.notification.event.NotificationSavedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final SimpMessagingTemplate messagingTemplate;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleNotificationSavedEvent(NotificationSavedEvent event) {
		messagingTemplate.convertAndSendToUser(
			event.principalName(),
			"/queue/notification",
			event.response()
		);
	}
}
