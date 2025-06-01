package org.ezcode.codetest.infrastructure.message.config;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

import org.ezcode.codetest.infrastructure.security.jwt.JwtUtilImpl;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomHandShakeHandler extends DefaultHandshakeHandler {

	private final JwtUtilImpl jwtUtilImpl;

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
		//토큰의 대한 예외처리 아직 구현 x

		Claims claims = jwtUtilImpl.extractClaims(tokenParam);
		String email = claims.get("email", String.class);

		return () -> email;
	}
}
