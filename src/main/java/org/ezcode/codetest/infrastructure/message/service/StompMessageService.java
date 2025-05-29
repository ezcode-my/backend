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

	/****
	 * 사용자가 입장할 때 해당 사용자에게 STOMP 메시지를 전송합니다.
	 *
	 * @param principalName 메시지를 받을 사용자의 이름
	 */
	public void handleEnter(String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chatroom",
			"pub 테스트"
		);
	}
}
