package org.ezcode.codetest.application.chatting.dto.response;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.chat.model.Chat;

public record ChatResponse(

	LocalDateTime time,

	String name,

	String tier,

	String message
) {
	public static ChatResponse from(Chat chat) {

		return new ChatResponse(
			chat.getCreatedAt(),
			chat.getUser().getNickname(),
			chat.getUser().getTier().toString(),
			chat.getMessage()
		);

	}
}
