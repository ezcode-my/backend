package org.ezcode.codetest.infrastructure.notification.service;

import static org.ezcode.codetest.infrastructure.notification.model.NotificationQueueConstants.*;

import java.util.List;

import org.ezcode.codetest.infrastructure.notification.model.NotificationProcessLog;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRetryScheduler {

	private final ProcessLogService processLogService;
	private final JmsTemplate jmsTemplate;

	@Scheduled(fixedRate = 300000)	// 5분마다 실행
	public void retryFailedNotifications() {

		log.info("실패할 알림 재처리를 시작합니다...");
		List<NotificationProcessLog> retryableJobs = processLogService.findRetryableJobs();

		for (NotificationProcessLog job : retryableJobs) {
			log.info("재처리 시도: messageId={}", job.getMessageId());

			jmsTemplate.convertAndSend(NOTIFICATION_QUEUE_CREATE, job.getPayload(), message -> {
				message.setStringProperty(CUSTOM_HEADER_MESSAGE_ID, job.getMessageId());
				return message;
			});
		}
	}
}
