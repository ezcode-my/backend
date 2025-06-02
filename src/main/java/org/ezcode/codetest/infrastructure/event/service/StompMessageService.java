package org.ezcode.codetest.infrastructure.event.service;

import org.ezcode.codetest.application.chatting.port.event.ChattingMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompMessageService implements ChattingMessageService {

	private final SimpMessagingTemplate messagingTemplate;

	public <T> void handleEnter(T roomData, String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chatrooms",
			roomData
		);
	}

	public <T> void handleRoomEnter(T chatData, String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chat",
			chatData
		);
	}

	public <T> void handleBroadCastChat(T data, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, data);
	}

	public <T> void handleRoomEnterAndLeftEvent(T MessageData, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, MessageData);
	}

	public <T> void handleRoomChangeEvent(T roomData) {

		messagingTemplate.convertAndSend("/topic/chatrooms", roomData);
	}

}
