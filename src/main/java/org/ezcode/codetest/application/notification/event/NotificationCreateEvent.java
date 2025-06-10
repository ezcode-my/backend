package org.ezcode.codetest.application.notification.event;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.dto.payload.NotificationPayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;

public record NotificationCreateEvent(

	String principalName,

	NotificationType notificationType,

	String message,

	NotificationPayload payload,

	String redirectUrl,

	boolean isRead,

	LocalDateTime createdAt

) {
}
