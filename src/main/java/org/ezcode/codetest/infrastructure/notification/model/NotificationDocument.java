package org.ezcode.codetest.infrastructure.notification.model;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationDocument {

	@Id
	private String id;

	private String principalName;

	private NotificationType notificationType;

	private NotificationPayload payload;

	private boolean isRead;

	private LocalDateTime createdAt;

	public static NotificationDocument from(NotificationCreateEvent event) {

		return new NotificationDocument(
			null,
			event.principalName(),
			event.notificationType(),
			event.payload(),
			event.isRead(),
			event.createdAt()
		);
	}

	public void markAsRead() {
		this.isRead = true;
	}
}
