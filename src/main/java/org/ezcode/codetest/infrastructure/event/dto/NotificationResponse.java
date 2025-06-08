package org.ezcode.codetest.infrastructure.event.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.ezcode.codetest.application.notification.enums.NotificationType;

import lombok.Builder;

@Builder
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
		return NotificationResponse
			.builder()
			.id(record.getId())
			.type(record.getType())
			.message(record.getMessage())
			.redirectUrl(record.getRedirectUrl())
			.payload(record.getPayload())
			.isRead(record.isRead())
			.createdAt(record.getCreatedAt())
			.build();
	}
}
