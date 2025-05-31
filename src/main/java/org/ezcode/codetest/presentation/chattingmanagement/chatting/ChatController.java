package org.ezcode.codetest.presentation.chattingmanagement.chatting;

import org.ezcode.codetest.application.chatting.dto.request.ChatSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
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
	public void createChat(@PathVariable Long roomId, @RequestBody ChatSaveRequest request) {

		//나중에 Authentication 에서 받아올수 있게끔 수정예정입니다
		chatUseCase.sendChatting(request, "chat27@naver.com", roomId);
	}
}
