package org.ezcode.codetest.infrastructure.security.jwt;

import java.io.IOException;

import org.ezcode.codetest.common.dto.AuthUser;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.stereotype.Component;
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

	private final JwtUtil jwtUtil;

	private static final String[] WHITE_LIST = {
		"/signin",
		"/signup"
	};

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String bearerToken = request.getHeader("Authorization");
		String url = request.getRequestURI();

		if(isWhiteList(url)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			if (request.getRequestURI().startsWith("/admin")){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "관리자 기능은 인증이 필요합니다");
			}
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = jwtUtil.substringToken(bearerToken);

		try {
			Claims claims = jwtUtil.extractClaims(jwt);

			if(claims == null){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다");
				return;
			}

			UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

			if (url.startsWith("/admin") && !UserRole.ADMIN.equals(userRole)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다");
					return;
			}

			AuthUser authUser = new AuthUser(
				Long.parseLong(claims.getSubject()),
				(String) claims.get("email"),
				(String) claims.get("username"),
				(String) claims.get("nickname"),
				userRole
			);

			request.setAttribute("authUser", authUser);

			filterChain.doFilter(request, response);
		} catch (SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
		} catch (Exception e) {
			log.error("Internal server error", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private boolean isWhiteList(String requestURI) {
		return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
	}
}
