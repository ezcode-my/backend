package org.ezcode.codetest.application.chatting.port.message;

public interface MessageService {

	<T> void handleEnter(T roomData, String principalName);

	<T> void handleRoomEnter(T chatData,String principalName);

	<T> void handleBroadCastChat(T chatData, Long roomId);

	<T> void handleRoomEnterAndLeftEvent(T MessageData, Long roomId);

	<T> void handleRoomChangeEvent(T roomData);

}
