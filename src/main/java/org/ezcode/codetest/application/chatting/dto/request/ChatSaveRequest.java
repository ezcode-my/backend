package org.ezcode.codetest.application.chatting.dto.request;

import org.ezcode.codetest.domain.chat.model.Chat;
import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "채팅 메시지 생성 요청")
public record ChatSaveRequest(

	@Schema(description = "전송할 채팅 메시지")
	@NotBlank(message = "전송할 채팅 메시지는 공백일 수 없습니다.")
	@Size(message = "채팅방 메시지 길이는 100자 이내로 작성해주세요", max = 100)
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
