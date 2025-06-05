package org.ezcode.codetest.infrastructure.event.dto;

public record BroadCastChatEvent<T>(

	T chatData,

	Long roomId
) {

}
