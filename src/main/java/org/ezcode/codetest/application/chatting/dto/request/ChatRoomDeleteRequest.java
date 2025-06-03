package org.ezcode.codetest.application.chatting.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatRoomDeleteRequest(

	@NotNull(message = "삭제하려는 방 ID를 반드시 입력해야합니다")
	Long roomId
) {

}
