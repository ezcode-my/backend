package org.ezcode.codetest.application.notification.event.mapper;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;

public interface NotificationMapper<E> {
	Class<E> getSupportedType();

	NotificationCreateEvent map(E event);
}
