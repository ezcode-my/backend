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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@CircuitBreaker(name = "db-circuit", fallbackMethod = "createNewNotificationFallback")
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

	/**
	 * createNewNotification의 Fallback 메서드
	 * 서킷이 열리면 이 메서드가 대신 실행됨
	 * @param event 원본 메서드와 동일한 파라미터
	 * @param ex 발생한 예외
	 */
	private void createNewNotificationFallback(NotificationCreateEvent event, Throwable ex) {
		// 어떤 예외 때문에 서킷이 열렸는지 로그를 남김
		log.warn("Circuit Breaker is open for createNewNotification. Event: {}, Error: {}", event, ex.getMessage());

		//  메시지를 "처리 실패"로 알려서 MQ가 재시도하거나 DLQ로 보내도록 함
		throw new NotificationException(
			NotificationExceptionCode.NOTIFICATION_DB_ERROR,
			ex.getMessage()
		);
	}
}
