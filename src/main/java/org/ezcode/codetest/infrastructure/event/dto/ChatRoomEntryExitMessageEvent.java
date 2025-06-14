package org.ezcode.codetest.infrastructure.event.dto;

public record ChatRoomEntryExitMessageEvent<T>(

	T messageData,

	Long roomId
) {
}
