package org.ezcode.codetest.application.chatting.port.session;

import java.util.Map;

public interface ChattingSessionService {

	Long addSession(String sessionId, Long roomId);

	Map<String,Long> removeSession(String sessionId);

	Long viewSession(Long roomId);

	Long removeRoom(Long roomId);

}
