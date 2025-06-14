package org.ezcode.codetest.infrastructure.event.service;

import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.infrastructure.event.dto.ChatMessageBroadcastEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomListLoadEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomParticipantCountChangeEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomEntryExitMessageEvent;
import org.ezcode.codetest.infrastructure.event.dto.ChatRoomHistoryLoadEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatEventPublisher implements ChatEventService {

	private final ApplicationEventPublisher publisher;

	public <T> void publishChatRoomListLoadEvent(T roomData, String principalName, String sessionId) {

		publisher.publishEvent(new ChatRoomListLoadEvent<>(roomData, principalName, sessionId));
	}

	public <T> void publishChatRoomHistoryLoadEvent(T chatData, String principalName, String sessionId) {

		publisher.publishEvent(new ChatRoomHistoryLoadEvent<>(chatData, principalName, sessionId));
	}

	public <T> void publishChatMessageBroadcastEvent(T chatData, Long roomId) {

		publisher.publishEvent(new ChatMessageBroadcastEvent<>(chatData, roomId));
	}

	public <T> void publishChatRoomEntryExitMessageEvent(T messageData, Long roomId) {

		publisher.publishEvent(new ChatRoomEntryExitMessageEvent<>(messageData, roomId));
	}

	public <T> void publishChatRoomParticipantCountChangeEvent(T roomData) {

		publisher.publishEvent(new ChatRoomParticipantCountChangeEvent<>(roomData));
	}
}
