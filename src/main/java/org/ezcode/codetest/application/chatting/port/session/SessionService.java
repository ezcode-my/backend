package org.ezcode.codetest.application.chatting.port.session;

import java.util.Map;

public interface SessionService {

	Long addSession(String sessionId, Long roomId);

	Map<String,Long> removeSession(String sessionId);

	Long viewSession(Long roomId);

}
