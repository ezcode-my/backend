package org.ezcode.codetest.application.chatting.port.event;

public interface ChatEventService {

	<T> void publishEnterEvent(T roomData, String principalName, String sessionId);

	<T> void publishRoomEnterEvent(T chatData, String principalName, String sessionId);

	<T> void publishBroadCastChatEvent(T chatData, Long roomId);

	<T> void publishRoomEnterAndLeftEvent(T messageData, Long roomId);

	<T> void publishRoomChangeEvent(T roomData);
}
