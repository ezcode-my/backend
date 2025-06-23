package org.ezcode.codetest.infrastructure.event.config;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomHandShakeHandler extends DefaultHandshakeHandler {

	private final JwtUtil jwtUtil;

	@Override
	protected Principal determineUser(
		@NonNull ServerHttpRequest request,
		@NonNull WebSocketHandler wsHandler,
		@NonNull Map<String, Object> attributes
	) {
		URI uri = request.getURI();
		String query = uri.getQuery();
		String tokenParam = null;

		if (query != null && query.startsWith("token=")) {
			tokenParam = query.substring(6);
		}

		Claims claims = jwtUtil.extractClaims(tokenParam);
		String email = claims.get("email", String.class);

		return () -> email;
	}
}
