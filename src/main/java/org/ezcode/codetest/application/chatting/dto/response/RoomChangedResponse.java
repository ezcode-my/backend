package org.ezcode.codetest.application.chatting.dto.response;

import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.ezcode.codetest.domain.chat.model.ChatRoom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "채팅방 변경에 대한 응답")
public record RoomChangedResponse(

	@Schema(description = "채팅방 ID")
	Long roomId,

	@Schema(description = "채팅방 제목")
	String title,

	@Schema(description = "채팅방 인원수")
	Long headCount,

	@Schema(description = "이벤트 타입(삭제, 변경, 생성)")
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
