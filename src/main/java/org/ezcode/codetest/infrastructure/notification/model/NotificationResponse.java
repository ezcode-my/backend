package org.ezcode.codetest.infrastructure.notification.model;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;

public record NotificationResponse(

	String id,

	NotificationType notificationType,

	NotificationPayload payload,

	boolean isRead,

	LocalDateTime createdAt

) {

	public static NotificationResponse from(NotificationRecord record) {
		return new NotificationResponse(
			record.getId(),
			record.getType(),
			record.getPayload(),
			record.isRead(),
			record.getCreatedAt()
		);
	}

	public static NotificationResponse from(NotificationDocument document) {

		return new NotificationResponse(
			document.getId(),
			document.getNotificationType(),
			document.getPayload(),
			document.isRead(),
			document.getCreatedAt()
		);
	}
}
