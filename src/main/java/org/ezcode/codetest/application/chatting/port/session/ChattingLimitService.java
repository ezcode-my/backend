package org.ezcode.codetest.application.chatting.port.session;

public interface ChattingLimitService {

	Long increaseChatCount(String email);

	void applyChatBlock(String email);

	Boolean isBlocked(String email);

}
