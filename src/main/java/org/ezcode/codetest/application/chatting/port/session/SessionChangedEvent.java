package org.ezcode.codetest.application.chatting.port.session;

import lombok.Builder;

@Builder
public record SessionChangedEvent(

	Long roomId,

	String title,

	Long headCount,

	String eventType
) {
}
