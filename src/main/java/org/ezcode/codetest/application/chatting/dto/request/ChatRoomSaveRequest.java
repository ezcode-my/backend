package org.ezcode.codetest.application.chatting.dto.request;

import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRoomSaveRequest(

	@NotBlank(message = "채팅방 제목은 비어있을 수 없습니다")
	@Size(message = "채팅방 이름 길이는 1~15자 사이로 작성해주세요", min = 1, max = 15)
	String title
) {

	public ChatRoom toEntity(User user) {

		return ChatRoom.builder()
			.title(title)
			.isDeleted(false)
			.user(user)
			.build();
	}
}
