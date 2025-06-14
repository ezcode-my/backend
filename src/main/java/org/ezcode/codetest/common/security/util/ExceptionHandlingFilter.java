package org.ezcode.codetest.common.security.util;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandlingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
		} catch (JwtException e) {
			log.warn("JWT 관련 예외", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 처리 오류입니다.");
		} catch (IOException | ServletException e) {
			throw e;
		} catch (Exception e) {
			// 서비스 예외는 컨트롤러까지 전달
			throw e;
		}

	}
}

