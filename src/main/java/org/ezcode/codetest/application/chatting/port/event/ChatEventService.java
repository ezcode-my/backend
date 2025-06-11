package org.ezcode.codetest.application.chatting.port.event;

public interface ChatEventService {

	<T> void publishChatRoomListLoadEvent(T roomData, String principalName, String sessionId);

	<T> void publishChatRoomHistoryLoadEvent(T chatData, String principalName, String sessionId);

	<T> void publishChatMessageBroadcastEvent(T chatData, Long roomId);

	<T> void publishChatRoomEntryExitMessageEvent(T messageData, Long roomId);

	<T> void publishChatRoomParticipantCountChangeEvent(T roomData);
}
