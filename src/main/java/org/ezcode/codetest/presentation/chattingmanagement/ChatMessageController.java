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

	@MessageMapping("/enter")
	public void handleEnter(Principal principal, @Payload String payload) {

		System.out.println(payload);

		messageService.handleEnter(principal.getName());


	}
}