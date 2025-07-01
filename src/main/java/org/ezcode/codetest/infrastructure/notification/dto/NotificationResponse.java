package org.ezcode.codetest.infrastructure.notification.dto;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;

public record NotificationResponse(

	String id,

	NotificationType notificationType,

	NotificationPayload payload,

	boolean isRead,

	LocalDateTime createdAt

) {

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
