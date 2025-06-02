package org.ezcode.codetest.infrastructure.security.jwt;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.ezcode.codetest.application.usermanagement.auth.port.JwtUtil;
import org.ezcode.codetest.common.exception.ServerException;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtilImpl implements JwtUtil {
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

	public String createToken(Long userId, String email, UserRole userRole, String username, String nickname, Tier tier) {
		if (userId == null || email == null || username == null || nickname == null) {
			throw new IllegalArgumentException("토큰에 필요한 필수 매개변수가 null입니다.");
		}

		Date date = new Date();
		log.info("-----------jwtUtil 토큰 생성 시작---------------");

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.claim("username", username)
				.claim("nickname", nickname)
				.claim("userRole", userRole == null ? UserRole.USER : userRole)
				.claim("tier", tier == null ? Tier.NEWBIE : tier)
				.setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION_TIME * 1000L)) //밀리초 단위
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

	/*
	Jwt 파싱 실패 예외 추가
	 */
	public Claims extractClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (io.jsonwebtoken.JwtException e) {
			log.error("JWT 토큰 파싱 실패: {}", e.getMessage());
			throw new ServerException("유효한 토큰이 아닙니다");
		}
	}
}
