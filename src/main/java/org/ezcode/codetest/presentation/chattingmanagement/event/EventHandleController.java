package org.ezcode.codetest.presentation.chattingmanagement.event;

import java.security.Principal;

import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EventHandleController {

	private final ChattingUseCase chatUseCase;

	@MessageMapping("/enter")
	public void handleEnter(Principal principal) {

		chatUseCase.getChatRoomList(principal.getName());
	}

	@MessageMapping("/room/{roomId}/enter")
	public void handleChatRoomEnter(
		@DestinationVariable Long roomId,
		Principal principal,
		SimpMessageHeaderAccessor accessor
	) {
		String sessionId = accessor.getSessionId();

		//나중에 Authentication 에서 받아올수 있게끔 수정예정입니다
		chatUseCase.getChattingHistory(sessionId, principal.getName(), "chat27@naver.com", roomId);
	}

	@MessageMapping("/room/{roomId}/left")
	public void handleChatRoomLeft(
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor
	) {
		String sessionId = accessor.getSessionId();

		//나중에 Authentication 에서 받아올수 있게끔 수정예정입니다
		chatUseCase.leftChatRoom(sessionId, "chat27@naver.com", roomId);
	}
}