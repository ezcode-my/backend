package org.ezcode.codetest.infrastructure.event.dto;

public record ChatRoomParticipantCountChangeEvent<T>(

	T roomData
) {
}
