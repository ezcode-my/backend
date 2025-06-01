package org.ezcode.codetest.presentation.chattingmanagement.chatting;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.ezcode.codetest.common.annotation.Auth;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
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
	public ResponseEntity<Void> createChatRoom(
		@Auth AuthUser authUser,
		@RequestBody @Validated ChatRoomSaveRequest request
	) {
		chatUseCase.createChatRoom(request, authUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
