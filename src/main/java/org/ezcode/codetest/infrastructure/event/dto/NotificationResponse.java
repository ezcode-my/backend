package org.ezcode.codetest.infrastructure.event.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.ezcode.codetest.application.notification.enums.NotificationType;

public record NotificationResponse(

	String id,

	NotificationType type,

	String message,

	String redirectUrl,

	Map<String, Object> payload,

	boolean isRead,

	LocalDateTime createdAt

) {

	public static NotificationResponse from(NotificationRecord record) {
		return new NotificationResponse(
			record.getId(),
			record.getType(),
			record.getMessage(),
			record.getRedirectUrl(),
			record.getPayload(),
			record.isRead(),
			record.getCreatedAt()
		);
	}
}
