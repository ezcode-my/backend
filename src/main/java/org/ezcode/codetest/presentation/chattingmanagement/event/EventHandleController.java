package org.ezcode.codetest.presentation.chattingmanagement.event;

import java.security.Principal;

import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

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
	public void handleChatRoomEnter(@PathVariable Long roomId, Principal principal,
		SimpMessageHeaderAccessor accessor) {
		String sessionId = accessor.getSessionId();

		chatUseCase.getChattingHistory(sessionId, principal.getName(), 1L, roomId);

	}

	@MessageMapping("/room/{roomId}/left")
	public void handleChatRoomLeft(@PathVariable Long roomId, SimpMessageHeaderAccessor accessor) {
		String sessionId = accessor.getSessionId();

		//나중에 @AuthenticationPrincipal 로 userId 획득해서 넣을 예정
		chatUseCase.leftChatRoom(sessionId, 1L, roomId);

	}
}