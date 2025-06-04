package org.ezcode.codetest.application.chatting.dto.response;

import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.ezcode.codetest.domain.chat.model.ChatRoom;

import lombok.Builder;

@Builder
public record RoomChangedResponse(

	Long roomId,

	String title,

	Long headCount,

	String eventType
) {

	public static RoomChangedResponse from(ChatRoom room, String eventType, Long headCount) {

		return RoomChangedResponse.builder()
			.roomId(room.getId())
			.title(room.getTitle())
			.eventType(eventType)
			.headCount(headCount)
			.build();
	}

	public static RoomChangedResponse from(ChatRoom room, String eventType) {

		return RoomChangedResponse.builder()
			.roomId(room.getId())
			.title(room.getTitle())
			.eventType(eventType)
			.headCount(0L)
			.build();
	}

	public static RoomChangedResponse from(ChatRoomCache room, String eventType, Long headCount) {

		return RoomChangedResponse.builder()
			.roomId(room.roomId())
			.title(room.title())
			.eventType(eventType)
			.headCount(headCount)
			.build();
	}
}
