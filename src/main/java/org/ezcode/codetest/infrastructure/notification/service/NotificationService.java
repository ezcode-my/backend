package org.ezcode.codetest.infrastructure.notification.service;

import java.util.List;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationPageResponse;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private final NotificationMongoRepository mongoRepository;

	public NotificationService(
		NotificationMongoRepository mongoRepository
	) {
		this.mongoRepository = mongoRepository;
	}

	public NotificationPageResponse<NotificationResponse> getNotifications(NotificationListRequestEvent event) {

		PageRequest pageRequest = PageRequest.of(event.page(), event.size(), Sort.by("createdAt").descending());

		Page<NotificationDocument> page = mongoRepository.findAllByPrincipalName(event.principalName(), pageRequest);

		List<NotificationResponse> notificationList = page.stream()
			.map(NotificationResponse::from)
			.toList();

		return new NotificationPageResponse<>(
			notificationList,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements()
		);
	}

	public NotificationDocument createNewNotification(NotificationCreateEvent event) {

		return mongoRepository.save(NotificationDocument.from(event));
	}

	public void markAsRead(NotificationMarkReadEvent event) {

		NotificationDocument notificationDocument = mongoRepository
			.findByIdAndPrincipalName(event.notificationId(), event.principalName())
			.orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

		notificationDocument.markAsRead();
		mongoRepository.save(notificationDocument);
	}
}
