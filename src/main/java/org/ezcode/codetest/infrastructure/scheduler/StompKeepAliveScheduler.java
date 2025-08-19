package org.ezcode.codetest.infrastructure.scheduler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StompKeepAliveScheduler {

	private final SimpMessagingTemplate messagingTemplate;

	public StompKeepAliveScheduler(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	// 15초마다 실행 (ActiveMQ TTL인 20초보다 짧게 설정)
	@Scheduled(fixedDelay = 15000)
	public void sendKeepAliveMessage() {
		this.messagingTemplate.convertAndSend("/topic/keep-alive", "ping");
	}
}
