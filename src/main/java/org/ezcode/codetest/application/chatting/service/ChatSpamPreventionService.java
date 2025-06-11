package org.ezcode.codetest.application.chatting.service;

import org.ezcode.codetest.application.chatting.port.event.ChatEventService;
import org.ezcode.codetest.application.chatting.port.session.ChatLimitService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatSpamPreventionService {

	private final ChatLimitService limitService;
	private final ChatEventService eventService;

	public Boolean isChatBlocked(String email) {

		return limitService.isBlocked(email);
	}

	public void applyChatBlock(String email, String nickName, Long roomId) {

		limitService.applyChatSpamPenalty(email);

		eventService.publishChatMessageBroadcastEvent(ChatMessageTemplate.SPAM_BLOCK.format(nickName, 30), roomId);
	}

	public Long countChatsInLast10Seconds(String email) {

		return limitService.increaseChatCount(email);
	}
}
