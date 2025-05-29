package org.ezcode.codetest.presentation.chattingmanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatViewController {

	/**
	 * 채팅 페이지 뷰를 반환합니다.
	 *
	 * @return 채팅 페이지의 뷰 이름("chat-page")
	 */
	@GetMapping("/chatting")
	public String getChattingPage() {

		return "chat-page";
	}
}
