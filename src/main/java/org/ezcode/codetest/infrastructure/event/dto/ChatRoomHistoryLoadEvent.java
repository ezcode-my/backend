package org.ezcode.codetest.infrastructure.event.dto;

public record ChatRoomHistoryLoadEvent<T>(

	T chatData,

	String principalName,

	String sessionId
) {
}
