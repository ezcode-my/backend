package org.ezcode.codetest.presentation.chatting.event;

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
	public void handleGetChatRoomList(
		Principal principal,
		SimpMessageHeaderAccessor accessor
	) {
		String sessionId = accessor.getSessionId();
		chatUseCase.getChatRoomList(principal.getName(), sessionId);
	}

	@MessageMapping("/rooms/{roomId}/enter")
	public void handleGetChattingHistory(
		Principal principal,
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor
	) {
		String sessionId = accessor.getSessionId();
		chatUseCase.getChattingHistory(sessionId, principal.getName(), principal.getName(), roomId);
	}

	@MessageMapping("/rooms/{roomId}/left")
	public void handleChatRoomLeft(
		Principal principal,
		@DestinationVariable Long roomId,
		SimpMessageHeaderAccessor accessor
	) {
		String sessionId = accessor.getSessionId();
		chatUseCase.leftChatRoom(sessionId, principal.getName(), roomId);
	}
}