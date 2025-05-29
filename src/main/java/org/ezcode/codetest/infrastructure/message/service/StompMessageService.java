package org.ezcode.codetest.infrastructure.message.service;

import org.apache.logging.log4j.message.SimpleMessage;
import org.ezcode.codetest.application.chatting.port.message.MessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompMessageService implements MessageService {

	private final SimpMessagingTemplate messagingTemplate;

	//TODO : STOMP 기반 메시지 서비스 구현체 구현 예정
	public void handleEnter(String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chatroom",
			"pub 테스트"
		);
	}
}
