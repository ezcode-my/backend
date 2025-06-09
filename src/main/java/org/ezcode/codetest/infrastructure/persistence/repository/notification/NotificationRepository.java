package org.ezcode.codetest.infrastructure.persistence.repository.notification;

import java.util.List;

import org.ezcode.codetest.infrastructure.event.dto.NotificationRecord;

public interface NotificationRepository {

	void save(NotificationRecord record);

	List<NotificationRecord> findAll(String principalName, int page, int size);

	void markAsRead(String principalName, String notificationId);

}
