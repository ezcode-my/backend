package org.ezcode.codetest.common.security.util;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
public class JwtUtil {
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 7;
	private static final long GAME_TOKEN_EXPIRATION_TIME = 60;

	@Value("${jwt.secret}")
	private String secretKey;

	private Key key;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	/*
	토큰 발급
	 */
	public String createAccessToken(Long userId, String email, UserRole userRole, String username, String nickname, Tier tier) {
		if (userId == null || email == null || username == null || nickname == null) {
			throw new IllegalArgumentException("토큰에 필요한 필수 매개변수가 null입니다.");
		}

		Date date = new Date();

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

	public String createGameToken(Long eventId) {

		if (eventId == null) {
			throw new IllegalArgumentException("토큰에 필요한 필수 매개변수가 null입니다.");
		}

		Date date = new Date();

		return Jwts.builder()
			.setSubject(String.valueOf(eventId))
			.setExpiration(new Date(date.getTime() + GAME_TOKEN_EXPIRATION_TIME * 2000L))
			.setIssuedAt(date)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	/*
	토큰 추출
	 */
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

	//토큰 유효시간 계산
	public Long getExpiration(String token) {
		Claims claims = extractClaims(token);
		Date expiration = claims.getExpiration();
		return expiration.getTime() - System.currentTimeMillis(); // 남은시간을 ms 단위로 계산해서 반환
	}

	public Long getRemainingTime(String token) {
		Date expiration = extractClaims(token).getExpiration();
		return expiration.getTime() - System.currentTimeMillis();
	}

	/*
	토큰 갱신
	 */
	public String createRefreshToken(Long userId) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + TOKEN_EXPIRATION_TIME * 1000L); //만료 시간

		return Jwts.builder()
			.setSubject(String.valueOf(userId)) // 주제설정, string 으로만 주입 가능
			.setIssuedAt(now) // 발급날짜
			.setExpiration(expirationDate) // 만료날짜
			.signWith(key, signatureAlgorithm) // 암호화 알고리즘
			.compact();
	}

	public Long getUserId(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String refreshToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

    public String createEmailToken(Long userId, String email) {
		if ( email == null ) {
			throw new IllegalArgumentException("토큰에 필요한 필수 매개변수가 null입니다.");
		}

		Date date = new Date();
		long EXPIRATION_TIME = 600 * 1000; // 10분

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.setExpiration(new Date(date.getTime() + EXPIRATION_TIME))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

}
