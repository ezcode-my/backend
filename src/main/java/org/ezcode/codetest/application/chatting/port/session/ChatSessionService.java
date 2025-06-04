package org.ezcode.codetest.application.chatting.port.session;

import java.util.Map;

public interface ChatSessionService {

	Long addSessionCount(String sessionId, Long roomId);

	Map<String, Long> removeSessionCount(String sessionId);

	Long viewSessionCount(Long roomId);

	void removeRoomSession(Long roomId);

}
