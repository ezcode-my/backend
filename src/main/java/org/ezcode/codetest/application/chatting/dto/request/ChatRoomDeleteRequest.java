package org.ezcode.codetest.application.chatting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "채팅방 삭제 요청")
public record ChatRoomDeleteRequest(

	@Schema(description = "삭제할 채팅방 ID")
	@NotNull(message = "삭제하려는 방 ID를 반드시 입력해야합니다")
	@Min(value = 1, message = "ID 는 1 이상부터 입력가능합니다.")
	Long roomId

) {

}
