package org.ezcode.codetest.infrastructure.notification.service;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationMongoRepository mongoRepository;

	// @CacheEvict(value = "notificationList", key = "#event.principalName + ':*'")
	public NotificationDocument createNewNotification(NotificationCreateEvent event) {

		return mongoRepository.save(NotificationDocument.from(event));
	}

	// @Cacheable(value = "notificationList", key = "#principalName + ':' + #page")
	public Page<NotificationDocument> getNotifications(String principalName, int page, int size) {

		int zeroBasedPage = page > 0 ? page - 1 : 0;

		PageRequest pageRequest = PageRequest.of(zeroBasedPage, size, Sort.by("createdAt").descending());
		return mongoRepository.findAllByPrincipalName(principalName, pageRequest);
	}

	public void markAsRead(NotificationMarkReadEvent event) {

		NotificationDocument notificationDocument = mongoRepository
			.findByIdAndPrincipalName(event.notificationId(), event.principalName())
			.orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

		notificationDocument.markAsRead();
		mongoRepository.save(notificationDocument);
	}
}
