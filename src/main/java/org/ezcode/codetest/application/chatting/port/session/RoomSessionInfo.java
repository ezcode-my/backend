package org.ezcode.codetest.application.chatting.port.session;

import lombok.Builder;

@Builder
public record RoomSessionInfo(

	Long roomId,

	Long headCount
) {
}
