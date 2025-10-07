package org.ezcode.codetest.infrastructure.notification.service;

import static org.ezcode.codetest.infrastructure.notification.model.NotificationQueueConstants.*;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationPageResponse;
import org.ezcode.codetest.infrastructure.notification.model.NotificationDocument;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventListener {

	private final NotificationService notificationService;

	private final SimpMessagingTemplate messagingTemplate;

	private final ObjectMapper objectMapper;

	@Transactional
	@JmsListener(destination = NOTIFICATION_QUEUE_CREATE)
	public void handleNotificationCreateEvent(String message) {

		NotificationCreateEvent event = convertObject(message, NotificationCreateEvent.class);
		NotificationDocument notification = notificationService.createNewNotification(event);

		messagingTemplate.convertAndSendToUser(
			event.principalName(),
			"/queue/notification",
			NotificationResponse.from(notification)
		);
	}

	@JmsListener(destination = NOTIFICATION_QUEUE_LIST)
	public void handleNotificationListRequestEvent(String message) {

		NotificationListRequestEvent event = convertObject(message, NotificationListRequestEvent.class);
		NotificationPageResponse<NotificationResponse> notifications = notificationService.getNotifications(event);

		messagingTemplate.convertAndSendToUser(
			event.principalName(),
			"/queue/notifications",
			notifications
		);
	}

	@JmsListener(destination = NOTIFICATION_QUEUE_MARK_READ)
	public void handleNotificationReadEvent(String message) {

		NotificationMarkReadEvent event = convertObject(message, NotificationMarkReadEvent.class);
		notificationService.markAsRead(event);
	}

	private <T> T convertObject(String message, Class<T> targetClass) {

		try {
			T dto = objectMapper.readValue(message, targetClass);
			log.info("알림 객체 {} 변환 성공", targetClass.getSimpleName());
			return dto;
		} catch (JsonProcessingException ex) {
			log.error("알림 객체 {} 변환 실패", targetClass.getSimpleName(), ex);
			throw new NotificationException(
				NotificationExceptionCode.NOTIFICATION_CONVERT_MESSAGE_ERROR
			);
		}
	}
}
