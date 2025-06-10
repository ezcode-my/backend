package org.ezcode.codetest.infrastructure.event.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.ezcode.codetest.application.notification.enums.NotificationType;

import lombok.Builder;
import lombok.Getter;

/*
 * 인프라 구현체(Redis, Mongo 등)들이
 * 저장 및 조회용으로 공통으로 사용하는 DTO
 */
@Getter
public class NotificationRecord implements Serializable {

	private final String id;
	private final String principalName;
	private final NotificationType type;
	private final String message;
	private final String redirectUrl;
	private final NotificationPayload payload;
	private boolean isRead;
	private final LocalDateTime createdAt;

	@Builder
	public NotificationRecord(
		String id,
		String principalName,
		NotificationType type,
		String message,
		String redirectUrl,
		NotificationPayload payload,
		boolean isRead,
		LocalDateTime createdAt
	) {
		this.id = id;
		this.principalName = principalName;
		this.type = type;
		this.message = message;
		this.redirectUrl = redirectUrl;
		this.payload = payload;
		this.isRead = isRead;
		this.createdAt = createdAt;
	}

	public static NotificationRecord from(NotificationCreateEvent dto) {
		return NotificationRecord
			.builder()
			.id(UUID.randomUUID().toString())
			.principalName(dto.principalName())
			.type(dto.notificationType())
			.message(dto.message())
			.redirectUrl(dto.redirectUrl())
			.payload(dto.payload())
			.isRead(dto.isRead())
			.createdAt(dto.createdAt())
			.build();
	}

	public void setRead() {
		this.isRead = true;
	}
}
