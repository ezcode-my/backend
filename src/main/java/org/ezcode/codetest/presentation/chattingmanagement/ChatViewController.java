package org.ezcode.codetest.presentation.chattingmanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatViewController {

	@GetMapping("/chatting")
	public String getChattingPage() {

		return "chat-page";
	}
}
