package org.ezcode.codetest.application.chatting.dto.response;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.chat.model.Chat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 메시지 반환")
public record ChatResponse(

	@Schema(description = "채팅 메시지 생성날짜")
	LocalDateTime time,

	@Schema(description = "사용자 닉네임")
	String name,

	@Schema(description = "사용자 등급")
	String tier,

	@Schema(description = "채팅 메시지")
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
