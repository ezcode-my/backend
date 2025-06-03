package org.ezcode.codetest.infrastructure.security.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtilImpl jwtUtilImpl;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		// JwtAuthenticationFilter.java
		log.info("Authorization 헤더: {}", request.getHeader("Authorization"));

		String bearerToken = request.getHeader("Authorization");

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = jwtUtilImpl.substringToken(bearerToken);

		if (redisTemplate.opsForValue().get("LOGOUT:" + jwt) != null) {
			throw new AuthException(AuthExceptionCode.NO_AUTH_INFO);
		}

		Claims claims = jwtUtilImpl.extractClaims(jwt);

		if (claims == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다");
			return;
		}

		UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
		String tierClaim = claims.get("tier", String.class);
		Tier tier = tierClaim != null ? Tier.valueOf(tierClaim) : Tier.NEWBIE;

		AuthUser authUser = new AuthUser(
			Long.parseLong(claims.getSubject()),
			(String)claims.get("username"),
			(String)claims.get("nickname"),
			(String)claims.get("email"),
			userRole,
			tier
		);

		// 인가를 위한 권한 정보 저장
		Collection<? extends GrantedAuthority> authorities =
			List.of(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name()));

		// Spring Security 인증 토큰 생성
		Authentication authToken;
		authToken = new UsernamePasswordAuthenticationToken(authUser, null, authorities);

		// SecurityContextHolder(세션)에 토큰 담기
		SecurityContextHolder.getContext().setAuthentication(authToken);

		log.info("Authentication 등록됨: {}", SecurityContextHolder.getContext().getAuthentication());
		log.info("Principal: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());


		filterChain.doFilter(request, response);
		}

	}
