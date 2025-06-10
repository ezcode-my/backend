package org.ezcode.codetest.infrastructure.event.listener;

import java.util.List;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationReadEvent;
import org.ezcode.codetest.infrastructure.event.dto.NotificationRecord;
import org.ezcode.codetest.infrastructure.event.dto.NotificationResponse;
import org.ezcode.codetest.infrastructure.event.service.StompMessageService;
import org.ezcode.codetest.infrastructure.persistence.repository.notification.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final NotificationRepository repository;
	private final StompMessageService messageService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleNotificationCreateEvent(NotificationCreateEvent dto) {

		NotificationRecord record = NotificationRecord.from(dto);
		repository.save(record);
		messageService.handleNotification(NotificationResponse.from(record), dto.principalName());
	}

	@EventListener
	public void handleNotificationListRequestEvent(NotificationListRequestEvent dto) {

		List<NotificationRecord> notificationList = repository.findAll(dto.principalName(), dto.page(), dto.size());
		messageService.handleNotificationList(
			notificationList.stream().map(NotificationResponse::from).toList(),
			dto.principalName()
		);
	}

	@EventListener
	public void handleNotificationReadEvent(NotificationReadEvent dto) {

		repository.markAsRead(dto.principalName(), dto.notificationId());
	}
}
