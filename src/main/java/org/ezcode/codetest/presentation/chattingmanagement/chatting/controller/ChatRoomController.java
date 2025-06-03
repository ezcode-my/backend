package org.ezcode.codetest.presentation.chattingmanagement.chatting.controller;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomDeleteRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@ResponseMessage("정상적으로 채팅방이 생성되었습니다.")
	@PostMapping
	public ResponseEntity<Void> createChatRoom(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Validated ChatRoomSaveRequest request
	) {
		chatUseCase.createChatRoom(request, authUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@ResponseMessage("정상적으로 채팅방이 삭제되었습니다.")
	@DeleteMapping
	public ResponseEntity<Void> removeChatRoom(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Validated ChatRoomDeleteRequest request
	) {
		chatUseCase.removeChatRoom(request, authUser.getEmail());

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
