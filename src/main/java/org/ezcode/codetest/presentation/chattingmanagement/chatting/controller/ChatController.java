package org.ezcode.codetest.presentation.chattingmanagement.chatting.controller;

import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/room/{roomId}/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChattingUseCase chatUseCase;

	@PostMapping
	@ResponseBody
	public void createChat(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long roomId,
		@RequestBody ChatSaveRequest request
	) {
		chatUseCase.sendChatting(request, authUser.getEmail(), roomId);
	}
}
