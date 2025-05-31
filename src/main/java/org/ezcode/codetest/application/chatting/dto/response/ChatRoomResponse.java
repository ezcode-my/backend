package org.ezcode.codetest.application.chatting.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomResponse(

	Long roomId,

	String title,

	Long headCount
) {
}
