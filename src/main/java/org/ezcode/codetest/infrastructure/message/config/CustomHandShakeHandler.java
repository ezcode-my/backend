package org.ezcode.codetest.infrastructure.message.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import lombok.NonNull;

public class CustomHandShakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(
		@NonNull ServerHttpRequest request,
		@NonNull WebSocketHandler wsHandler,
		@NonNull Map<String, Object> attributes
	) {
		String sessionId = UUID.randomUUID().toString();
		return () -> sessionId;
	}
}
