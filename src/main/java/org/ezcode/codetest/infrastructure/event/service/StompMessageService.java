package org.ezcode.codetest.infrastructure.event.service;

import java.util.List;

import org.ezcode.codetest.infrastructure.event.dto.NotificationResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompMessageService {

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

	public void handleNotification(NotificationResponse data, String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/notification",
			data
		);
	}

	public void handleNotificationList(List<NotificationResponse> dataList, String principalName) {

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/notifications",
			dataList
		);
	}

	public <T> void handleBroadCastChat(T data, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, data);
	}

	public <T> void handleRoomEnterAndLeftEvent(T messageData, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, messageData);
	}

	public <T> void handleRoomChangeEvent(T roomData) {

		messagingTemplate.convertAndSend("/topic/chatrooms", roomData);
	}

}
