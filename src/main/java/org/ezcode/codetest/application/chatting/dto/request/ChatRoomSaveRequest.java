package org.ezcode.codetest.application.chatting.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRoomSaveRequest(
	@NotBlank(message = "채팅방 제목은 비어있을 수 없습니다")
	@Size(message = "채팅방 이름 길이는 1~8자 사이로 작성해주세요", min = 1, max = 8)
	String title
) {
}
