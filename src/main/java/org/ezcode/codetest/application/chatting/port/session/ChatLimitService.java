package org.ezcode.codetest.application.chatting.port.session;

public interface ChatLimitService {

	Long increaseChatCount(String email);

	void applyChatSpamPenalty(String email);

	Boolean isBlocked(String email);

}
