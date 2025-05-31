package org.ezcode.codetest.presentation.chattingmanagement.chatting;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
public class ChatRoomController {

	private final ChattingUseCase chatUseCase;

	@PostMapping
	public ResponseEntity<Void> createChatRoom(@RequestBody @Validated ChatRoomSaveRequest request) {

		//나중에 Authentication 에서 받아올수 있게끔 수정예정입니다
		chatUseCase.createChatRoom(request, "chat27@naver.com");

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
