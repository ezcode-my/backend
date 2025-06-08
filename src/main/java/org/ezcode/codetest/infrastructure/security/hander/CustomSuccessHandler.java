package org.ezcode.codetest.infrastructure.security.hander;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.port.JwtUtil;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtilImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtilImpl jwtUtil;
	private final UserDomainService userDomainService;
	private final RedisTemplate<String, String> redisTemplate;

	public CustomSuccessHandler(JwtUtilImpl jwtUtil, UserDomainService userDomainService,
		RedisTemplate<String, String> redisTemplate) {
		this.jwtUtil = jwtUtil;
		this.userDomainService = userDomainService;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
		IOException {

		//OAuth2User
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

		User loginUser = userDomainService.getOAuthUser(customUserDetails.getEmail(), customUserDetails.getProvider());

		String token = jwtUtil.createToken(
			loginUser.getId(),
			loginUser.getEmail(),
			loginUser.getRole(),
			loginUser.getUsername(),
			loginUser.getNickname(),
			loginUser.getTier()
		);
		String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
		log.info("refresh token 발급 완료");
		//redis에 LOGIN : {redisToken} 형식으로 저장
		redisTemplate.opsForValue().set(
			"RefreshToken:" + loginUser.getId(),
			refreshToken,
			jwtUtil.getExpiration(refreshToken),
			TimeUnit.MILLISECONDS);
		log.info("레디스에 저장완료");


		String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
		response.addCookie(createCookie("Authorization", encodedToken));

		response.sendRedirect("https://ezcode.my/");
	}


	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60*60*60);
		//cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}

}