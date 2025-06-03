package org.ezcode.codetest.infrastructure.security.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtilImpl jwtUtilImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String bearerToken = request.getHeader("Authorization");
		String url = request.getRequestURI();

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
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

			AuthUser authUser = new AuthUser(
				Long.parseLong(claims.getSubject()),
				(String) claims.get("username"),
				(String) claims.get("nickname"),
				(String) claims.get("email"),
				userRole,
				tier
			);

			// 인가를 위한 권한 정보 저장
			Collection<? extends GrantedAuthority> authorities =
				List.of(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name()));

			// Spring Security 인증 토큰 생성
			Authentication authToken = new UsernamePasswordAuthenticationToken(authUser,
				null, authorities);

			// SecurityContextHolder(세션)에 토큰 담기
			SecurityContextHolder.getContext().setAuthentication(authToken);


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

}
