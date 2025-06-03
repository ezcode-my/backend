package org.ezcode.codetest.application.chatting.port.event;

import lombok.Builder;

@Builder
public record ChatRoomChange(

	Long roomId,

	String title,

	Long headCount,

	String eventType
) {
}
