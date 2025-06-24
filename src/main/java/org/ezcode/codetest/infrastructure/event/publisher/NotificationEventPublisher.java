package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationReadEvent;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher implements NotificationEventService {

	private final ApplicationEventPublisher publisher;

	@Override
	public void saveAndNotify(NotificationCreateEvent dto) {

		publisher.publishEvent(dto);
	}

	@Override
	public void notifyList(NotificationListRequestEvent dto) {

		publisher.publishEvent(dto);
	}

	@Override
	public void setRead(NotificationReadEvent dto) {

		publisher.publishEvent(dto);
	}
}
