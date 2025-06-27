package org.ezcode.codetest.common.security.hander;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.dto.response.OAuthResponse;
import org.ezcode.codetest.common.security.util.AESUtil;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.CustomOAuth2User;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

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
	private final OAuth2AuthorizedClientService authorizedClientService;
	private final AESUtil aesUtil;

	public CustomSuccessHandler(JwtUtil jwtUtil, UserDomainService userDomainService,
		RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
        OAuth2AuthorizedClientService authorizedClientService, AESUtil aesUtil) {
		this.jwtUtil = jwtUtil;
		this.userDomainService = userDomainService;
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
        this.authorizedClientService = authorizedClientService;
        this.aesUtil = aesUtil;
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
		IOException {

		//OAuth2User
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

		User loginUser= userDomainService.getUserByEmail(customUserDetails.getEmail());
		log.info("loginUser Name: {}", loginUser.getUsername());

		if (customUserDetails.getProvider().equalsIgnoreCase("github")) {
			//깃허브 access-token 가져오기
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
				oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName()
			);

			UserGithubInfo userGithubInfo = userDomainService.getUserGithubInfoById(loginUser.getId());

			//AES 암호화
            try {
				String encodedGithubToken = aesUtil.encrypt(client.getAccessToken().getTokenValue());

				userGithubInfo.setGithubAccessToken(encodedGithubToken);
            } catch (Exception e) {
				log.error(e.getMessage());
                throw new AuthException(AuthExceptionCode.TOKEN_ENCODE_FAIL);
            }
			userDomainService.updateUserGithubAccessToken(userGithubInfo);
		}

		String accessToken = jwtUtil.createAccessToken(
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

		String redirectUri = (String) request.getSession().getAttribute("redirect_uri");

		if (redirectUri == null) {
			throw new AuthException(AuthExceptionCode.REDIRECT_URI_NOT_FOUND);
		}

		request.getSession().removeAttribute("redirect_uri"); // uri 사용 후 제거하기

		String targetUri = UriComponentsBuilder.fromUriString(redirectUri)
			.queryParam("accessToken", accessToken)
			.queryParam("refreshToken", refreshToken)
			.build().toUriString();

		//JSON 문자열로 바꿔서 클라이언트에게 응답 본문으로 전달
		OAuthResponse oAuthResponse = new OAuthResponse(accessToken, refreshToken);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(oAuthResponse));
		// response.sendRedirect(targetUri);
		}

}