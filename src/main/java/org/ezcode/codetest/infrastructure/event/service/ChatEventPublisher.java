package org.ezcode.codetest.infrastructure.event.service;

import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.infrastructure.event.dto.BroadCastChatEvent;
import org.ezcode.codetest.infrastructure.event.dto.EnterEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomChangeEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomEnterAndLeftEvent;
import org.ezcode.codetest.infrastructure.event.dto.RoomEnterEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatEventPublisher implements ChatEventService {

	private final ApplicationEventPublisher publisher;

	public <T> void publishEnterEvent(T roomData, String principalName) {

		publisher.publishEvent(new EnterEvent<>(roomData, principalName));
	}

	public <T> void publishRoomEnterEvent(T chatData, String principalName) {

		publisher.publishEvent(new RoomEnterEvent<>(chatData, principalName));
	}

	public <T> void publishBroadCastChatEvent(T chatData, Long roomId) {

		publisher.publishEvent(new BroadCastChatEvent<>(chatData, roomId));
	}

	public <T> void publishRoomEnterAndLeftEvent(T messageData, Long roomId) {

		publisher.publishEvent(new RoomEnterAndLeftEvent<>(messageData, roomId));
	}

	public <T> void publishRoomChangeEvent(T roomData) {

		publisher.publishEvent(new RoomChangeEvent<>(roomData));
	}
}
