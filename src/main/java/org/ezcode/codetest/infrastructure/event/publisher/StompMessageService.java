package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.infrastructure.event.dto.submission.response.ErrorWsResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.SubmissionFinalResultResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.InitTestcaseListResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.JudgeResultResponse;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

import java.util.List;

import org.ezcode.codetest.infrastructure.event.dto.NotificationResponse;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompMessageService {

	private final SimpMessagingTemplate messagingTemplate;

	public <T> void handleChatRoomListLoad(T roomData, String principalName, String sessionId) {

		SimpMessageHeaderAccessor accessor =
			SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

		accessor.setLeaveMutable(true);
		accessor.setSessionId(sessionId);

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chatrooms",
			roomData,
			accessor.getMessageHeaders()
		);
	}

	public <T> void handleChatRoomHistoryLoad(T chatData, String principalName, String sessionId) {

		SimpMessageHeaderAccessor accessor =
			SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

		accessor.setLeaveMutable(true);
		accessor.setSessionId(sessionId);

		messagingTemplate.convertAndSendToUser(
			principalName,
			"/queue/chat",
			chatData,
			accessor.getMessageHeaders()
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

	public void sendInitTestcases(String sessionKey, List<InitTestcaseListResponse> dataList) {

		messagingTemplate.convertAndSend(
			"/topic/submission/" + sessionKey + "/init",
			dataList
		);
	}

	public void sendTestcaseResultUpdate(String sessionKey, JudgeResultResponse data) {

		messagingTemplate.convertAndSend(
			"/topic/submission/" + sessionKey + "/case",
			data
		);
	}

	public void sendFinalResult(String sessionKey, SubmissionFinalResultResponse data) {

		messagingTemplate.convertAndSend(
			"/topic/submission/" + sessionKey + "/final",
			data
		);
	}

	public void sendError(String sessionKey, ErrorWsResponse data) {

		messagingTemplate.convertAndSend(
			"/topic/submission/" + sessionKey + "/error",
			data
		);
	}

	public <T> void handleChatMessageBroadcast(T data, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, data);
	}

	public <T> void handleChatRoomEntryExitMessage(T messageData, Long roomId) {

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, messageData);
	}

	public <T> void handleChatRoomParticipantCountChange(T roomData) {

		messagingTemplate.convertAndSend("/topic/chatrooms", roomData);
	}

}
