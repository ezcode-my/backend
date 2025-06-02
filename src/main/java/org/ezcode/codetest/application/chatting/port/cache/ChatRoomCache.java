package org.ezcode.codetest.application.chatting.port.cache;

import org.ezcode.codetest.domain.chat.model.ChatRoom;

public record ChatRoomCache(

	Long roomId,

	String title
) {
	public static ChatRoomCache from(ChatRoom room) {

		return new ChatRoomCache(room.getId(), room.getTitle());
	}
}