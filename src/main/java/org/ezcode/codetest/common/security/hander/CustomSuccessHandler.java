package org.ezcode.codetest.common.security.hander;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.dto.response.OAuthResponse;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final UserDomainService userDomainService;
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper; //json직렬화

	public CustomSuccessHandler(JwtUtil jwtUtil, UserDomainService userDomainService,
		RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		this.jwtUtil = jwtUtil;
		this.userDomainService = userDomainService;
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
		IOException {

		//OAuth2User
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

		User loginUser = userDomainService.getOAuthUser(customUserDetails.getEmail(), customUserDetails.getProvider());

		String accessToken = jwtUtil.createToken(
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

		//JSON 문자열로 바꿔서 클라이언트에게 응답 본문으로 전달
		OAuthResponse oAuthResponse = new OAuthResponse(accessToken, refreshToken);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(oAuthResponse));
		log.info("------------- accessToken : {}, refreshToken : {} ------------", accessToken, refreshToken);
	}

}