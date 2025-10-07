package org.ezcode.codetest.infrastructure.notification.service;

import java.util.List;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationPageResponse;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;
import org.ezcode.codetest.infrastructure.notification.event.NotificationSavedEvent;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.repository.NotificationMongoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

	private final NotificationMongoRepository mongoRepository;
	private final ApplicationEventPublisher publisher;

	public NotificationService(
		NotificationMongoRepository mongoRepository, ApplicationEventPublisher publisher
	) {
		this.mongoRepository = mongoRepository;
		this.publisher = publisher;
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

	@Transactional
	public void createNewNotification(NotificationCreateEvent event) {

		NotificationDocument savedNotification = mongoRepository.save(NotificationDocument.from(event));

		publisher.publishEvent(new NotificationSavedEvent(
			event.principalName(),
			NotificationResponse.from(savedNotification)
		));
	}

	@Transactional
	public void markAsRead(NotificationMarkReadEvent event) {

		NotificationDocument notificationDocument = mongoRepository
			.findByIdAndPrincipalName(event.notificationId(), event.principalName())
			.orElseThrow(() -> new NotificationException(NotificationExceptionCode.NOTIFICATION_NOT_FOUND));

		notificationDocument.markAsRead();
		mongoRepository.save(notificationDocument);
	}
}
