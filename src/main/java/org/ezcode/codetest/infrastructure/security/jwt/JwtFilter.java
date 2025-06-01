package org.ezcode.codetest.infrastructure.security.jwt;

import java.io.IOException;

import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtilImpl jwtUtilImpl;

	private static final String[] WHITE_LIST = {
		"/signin",
		"/signup",
		"/chatting",
		"/ws"
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

		String jwt = jwtUtilImpl.substringToken(bearerToken);

		try {
			Claims claims = jwtUtilImpl.extractClaims(jwt);

			if(claims == null){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다");
				return;
			}

			UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
			String tierClaim = claims.get("tier", String.class);
			Tier tier = tierClaim != null ? Tier.valueOf(tierClaim) : Tier.NEWBIE;

			if (url.startsWith("/admin") && !UserRole.ADMIN.equals(userRole)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다");
					return;
			}

			//생성자 순서가 틀려서 다시 정렬해서 넣었습니다
			AuthUser authUser = new AuthUser(
				Long.parseLong(claims.getSubject()),
				(String) claims.get("username"),
				(String) claims.get("nickname"),
				(String) claims.get("email"),
				userRole,
				tier
			);

			log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());

			request.setAttribute("authUser", authUser);
			request.setAttribute("userId", Long.parseLong(claims.getSubject()));
			request.setAttribute("email", claims.get("email"));
			request.setAttribute("userRole", claims.get("userRole"));
			request.setAttribute("username", claims.get("username"));
			request.setAttribute("nickname", claims.get("nickname"));


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
