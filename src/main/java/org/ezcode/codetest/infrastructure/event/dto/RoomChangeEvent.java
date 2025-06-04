package org.ezcode.codetest.infrastructure.event.dto;

public record RoomChangeEvent<T>(

	T roomData
) {
}
