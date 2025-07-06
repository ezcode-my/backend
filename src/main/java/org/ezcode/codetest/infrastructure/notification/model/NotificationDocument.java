package org.ezcode.codetest.infrastructure.notification.model;

import java.time.LocalDateTime;

import org.ezcode.codetest.application.notification.enums.NotificationType;
import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.payload.NotificationPayload;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@CompoundIndexes({
	@CompoundIndex(name = "user_created_idx", def = "{'principalName' : 1, 'createdAt' : -1}")
})
public class NotificationDocument {

	@Id
	private String id;

	private String principalName;

	private NotificationType notificationType;

	private NotificationPayload payload;

	@JsonProperty("read")
	private boolean isRead;

	@Indexed(expireAfter = "180d")	// 약 6개월 동안만 보관
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
