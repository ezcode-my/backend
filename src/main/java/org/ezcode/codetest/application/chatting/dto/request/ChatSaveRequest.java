package org.ezcode.codetest.application.chatting.dto.request;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 메시지 생성 요청")
public record ChatSaveRequest(

	@Schema(description = "전송할 채팅 메시지")
	String message
) {

	public Chat toEntity(User user, ChatRoom room) {

		return Chat.builder()
			.chatRoom(room)
			.user(user)
			.message(message)
			.build();
	}

}
