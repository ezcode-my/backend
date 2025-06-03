package org.ezcode.codetest.application.chatting.service;

import org.ezcode.codetest.application.chatting.port.event.ChattingMessageService;
import org.ezcode.codetest.application.chatting.port.session.ChattingLimitService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatSpamPreventionService {

	private final ChattingLimitService limitService;
	private final ChattingMessageService messageService;

	public Boolean isChatBlocked(String email) {

		return limitService.isBlocked(email);
	}

	public void applyChatBlock(String email, String nickName, Long roomId) {

		limitService.applyChatBlock(email);

		messageService.handleBroadCastChat(nickName + " 님께서 지나친 도배로 30초 동안 차단되었습니다.", roomId);
	}

	public Long countChatsInLast10Seconds(String email) {

		return limitService.increaseChatCount(email);
	}
}
