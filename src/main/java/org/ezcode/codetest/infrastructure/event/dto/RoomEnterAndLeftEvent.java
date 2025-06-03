package org.ezcode.codetest.infrastructure.event.dto;

public record RoomEnterAndLeftEvent<T>(

	T messageData,

	Long roomId
) {
}
