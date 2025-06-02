package org.ezcode.codetest.application.chatting.dto.request;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.user.model.entity.User;

public record ChatSaveRequest(

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
