package org.ezcode.codetest.infrastructure.notification.publisher;

import static org.ezcode.codetest.infrastructure.notification.model.NotificationQueueConstants.*;

import org.ezcode.codetest.application.notification.event.*;
import org.ezcode.codetest.application.notification.exception.NotificationException;
import org.ezcode.codetest.application.notification.exception.NotificationExceptionCode;
import org.ezcode.codetest.application.notification.port.NotificationEventService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher implements NotificationEventService {

	private final JmsTemplate jmsTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void notify(NotificationCreateEvent dto) {

		sendMessage(NOTIFICATION_QUEUE_CREATE, dto);
		// publisher.publishEvent(dto);
	}

	@Override
	public void notifyList(NotificationListRequestEvent dto) {

		sendMessage(NOTIFICATION_QUEUE_LIST, dto);
		// publisher.publishEvent(dto);
	}

	@Override
	public void setRead(NotificationMarkReadEvent dto) {

		sendMessage(NOTIFICATION_QUEUE_MARK_READ, dto);
		// publisher.publishEvent(dto);
	}

	private void sendMessage(String destination, Object data) {

		try {
			String jsonMessage = objectMapper.writeValueAsString(data);

			jmsTemplate.convertAndSend(destination, jsonMessage);
			log.info("알림 메시지 전송 성공 ({}) : {}", destination, jsonMessage);
		} catch (JsonProcessingException ex) {
			log.error("알림 메시지 변환 및 전송 실패 : {}", ex.getMessage());
			throw new NotificationException(NotificationExceptionCode.NOTIFICATION_CONVERT_MESSAGE_ERROR);
		}
	}
}
