package org.ezcode.codetest.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.ezcode.codetest.common.exception.ServerException;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 7;

	@Value("${jwt.secret}")
	private String secretKey;

	private Key key;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String createToken(Long userId, String email, UserRole userRole, String username, String nickname) {
		Date date = new Date();
		log.info("토큰 생성 시작");

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.claim("username", username)
				.claim("nickName", nickname)
				.claim("userRole", userRole)
				.setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	public String substringToken(String token) {
		if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
			return token.substring(BEARER_PREFIX.length());
		}
		throw new ServerException("토큰이 없습니다");
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
