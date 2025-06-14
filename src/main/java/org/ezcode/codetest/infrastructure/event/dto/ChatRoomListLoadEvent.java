package org.ezcode.codetest.infrastructure.event.dto;

public record ChatRoomListLoadEvent<T>(

	T roomData,

	String principalName,

	String sessionId
) {
}
