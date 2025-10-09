package org.ezcode.codetest.infrastructure.notification.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "notification_process_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationProcessLog {

	@Id
	private String messageId;

	private String payload;

	private ProcessStatus status;

	private int retryCount;

	private String errorMessage;

	private LocalDateTime lastAttemptAt;

	private LocalDateTime createdAt;

	public enum ProcessStatus {
		PENDING, SUCCESS, FAILED, PERMANENTLY_FAILED
	}

	public static NotificationProcessLog of(String messageId, String payload) {

		return new NotificationProcessLog(
			messageId,
			payload,
			ProcessStatus.PENDING,
			0,
			null,
			LocalDateTime.now(),
			LocalDateTime.now()
		);
	}

	public void markAsSuccess() {
		this.status = ProcessStatus.SUCCESS;
		this.lastAttemptAt = LocalDateTime.now();
		this.errorMessage = null;
	}

	public void markAsFailed(String errorMessage, int maxRetries) {
		this.retryCount++;
		this.lastAttemptAt = LocalDateTime.now();
		this.errorMessage = errorMessage;

		if (this.retryCount >= maxRetries) {
			this.status = ProcessStatus.PERMANENTLY_FAILED;
		} else {
			this.status = ProcessStatus.FAILED;
		}
	}

	public void updateLastAttempt() {
		this.lastAttemptAt = LocalDateTime.now();
	}
}
