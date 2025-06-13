package org.ezcode.codetest.infrastructure.event.dto;

public record ChatMessageBroadcastEvent<T>(

	T chatData,

	Long roomId
) {

}
