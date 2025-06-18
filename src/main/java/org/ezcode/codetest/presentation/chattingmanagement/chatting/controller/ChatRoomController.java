package org.ezcode.codetest.presentation.chattingmanagement.chatting.controller;

import org.ezcode.codetest.application.chatting.dto.request.ChatRoomDeleteRequest;
import org.ezcode.codetest.application.chatting.dto.request.ChatRoomSaveRequest;
import org.ezcode.codetest.application.chatting.service.ChattingUseCase;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

	private final ChattingUseCase chatUseCase;

	@Operation(
		summary = "채팅방 생성 API",
		description = "현재 로그인한 사용자의 채팅방을 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "채팅방 생성 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 채팅방이 생성되었습니다.")
	@PostMapping
	public ResponseEntity<Void> createChatRoom(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid ChatRoomSaveRequest request
	) {
		chatUseCase.createChatRoom(request, authUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "채팅방 삭제 API",
		description = "현재 로그인한 사용자의 채팅방을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "채팅방 삭제 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 채팅방이 삭제되었습니다.")
	@DeleteMapping
	public ResponseEntity<Void> removeChatRoom(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid ChatRoomDeleteRequest request
	) {
		chatUseCase.removeChatRoom(request, authUser.getEmail());

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
