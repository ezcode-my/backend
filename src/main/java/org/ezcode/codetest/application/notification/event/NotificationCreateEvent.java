package org.ezcode.codetest.application.notification.event;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;

public record NotificationCreateEvent(

	String principalName,

	NotificationType notificationType,

	NotificationPayload payload,

	boolean isRead,

	LocalDateTime createdAt

) {

	public static NotificationCreateEvent of(String principalName, NotificationType notificationType, NotificationPayload payload) {
		return new NotificationCreateEvent(
			principalName,
			notificationType,
			payload,
			false,
			LocalDateTime.now()
		);
	}
}
