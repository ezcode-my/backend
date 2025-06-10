package org.ezcode.codetest.infrastructure.event.dto;

public record EnterEvent<T>(

	T roomData,

	String principalName,

	String sessionId
) {
}
