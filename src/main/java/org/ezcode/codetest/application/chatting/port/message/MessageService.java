package org.ezcode.codetest.application.chatting.port.message;

public interface MessageService {

	/**
 * 사용자가 채팅방에 입장할 때 필요한 처리를 수행합니다.
 *
 * @param principalName 입장하는 사용자의 이름
 */
void handleEnter(String principalName);
}
