package org.ezcode.codetest.application.chatting.port.session;

public interface ChatSessionService {

	Long addSessionCount(String sessionId, Long roomId);

	RoomSessionInfo removeSessionCount(String sessionId);

	Long viewSessionCount(Long roomId);

	void removeRoomSession(Long roomId);

}
