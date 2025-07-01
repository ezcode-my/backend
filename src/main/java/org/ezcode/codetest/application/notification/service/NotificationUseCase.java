package org.ezcode.codetest.application.notification.service;

import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationUseCase {

	private final NotificationEventService notificationEventService;

	public void getNotificationList(String email, Pageable pageable) {

		notificationEventService.notifyList(
			new NotificationListRequestEvent(email, pageable.getPageNumber(), pageable.getPageSize())
		);
	}

	public void modifyNotificationMarksRead(String email, String notificationId) {

		notificationEventService.setRead(
			new NotificationMarkReadEvent(email, notificationId)
		);
	}
}
