package org.ezcode.codetest.application.notification.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.ezcode.codetest.application.notification.enums.NotificationType;

import lombok.Builder;

@Builder
public record NotificationCreateEvent(

	String principalName,

	NotificationType notificationType,

	String message,

	Map<String, Object> payload,

	String redirectUrl,

	boolean isRead,

	LocalDateTime createdAt

) {
}
