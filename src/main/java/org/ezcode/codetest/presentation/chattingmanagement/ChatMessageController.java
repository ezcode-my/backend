package org.ezcode.codetest.presentation.chattingmanagement;

import java.security.Principal;

import org.ezcode.codetest.application.chatting.port.message.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

	private final MessageService messageService;

	/**
	 * 사용자가 채팅방에 입장할 때 호출되는 메시지 핸들러입니다.
	 *
	 * 인증된 사용자의 이름을 추출하여 입장 처리를 위임합니다.
	 *
	 * @param principal 인증된 사용자 정보를 포함하는 Principal 객체
	 * @param payload 클라이언트로부터 전달된 메시지 페이로드
	 */
	@MessageMapping("/enter")
	public void handleEnter(Principal principal, @Payload String payload) {

		System.out.println(payload);

		messageService.handleEnter(principal.getName());


	}
}