package org.ezcode.codetest.infrastructure.notification.service;

import static org.ezcode.codetest.infrastructure.notification.model.NotificationQueueConstants.*;

import org.ezcode.codetest.application.notification.event.NotificationCreateEvent;
import org.ezcode.codetest.application.notification.event.NotificationListRequestEvent;
import org.ezcode.codetest.application.notification.event.NotificationMarkReadEvent;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationPageResponse;
import org.ezcode.codetest.infrastructure.notification.dto.NotificationResponse;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationQueueConsumer {

	private final NotificationService notificationService;

	private final SimpMessagingTemplate messagingTemplate;

	private final ObjectMapper objectMapper;
	private final ProcessLogService processLogService;

	@JmsListener(destination = NOTIFICATION_QUEUE_CREATE)
	public void handleNotificationCreateEvent(Message<String> message) {

		String messageId = (String) message.getHeaders().get(CUSTOM_HEADER_MESSAGE_ID);
		String payload = message.getPayload();

		if (messageId == null) {
			log.error("커스텀 메시지 ID 헤더가 없어 메시지를 처리할 수 없습니다. payload={}", payload);
			return;
		}

		if (!processLogService.startProcessing(messageId, payload)) {
			log.warn("이미 처리되었거나 처리 중인 메시지입니다. messageId: {}", messageId);
			return;
		}

		try {
			NotificationCreateEvent event = convertObject(payload, NotificationCreateEvent.class);
			notificationService.createNewNotification(event);

			processLogService.finishProcessing(messageId);
		} catch (Exception e) {
			log.error("메시지 처리 실패. 재시도를 위해 FAILED로 기록합니다. messageId: {}", messageId);
			processLogService.failProcessing(messageId, e.getMessage());
		}
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
