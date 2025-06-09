package org.ezcode.codetest.infrastructure.event.dto;

public record RoomEnterEvent<T>(

	T chatData,

	String principalName,

	String sessionId
) {
}
