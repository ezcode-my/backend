package org.ezcode.codetest.application.usermanagement.auth.service;

import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.dto.response.RefreshTokenResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SignupResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.LogoutResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserDomainService userDomainService;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;


	/*
	이메일 회원가입
	- 이미 가입된 이메일 거절
	- 비밀번호 암호화
	 */
	@Transactional
	public SignupResponse signup(SignupRequest signupRequest) {
		userDomainService.checkEmailUnique(signupRequest.getEmail());

		if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())){
			throw new AuthException(AuthExceptionCode.PASSWORD_NOT_MATCH);
		}

		String encodedPassword = userDomainService.encodePassword(signupRequest.getPassword());


		User newUser = User.emailUser(
			signupRequest.getEmail(),
			encodedPassword,
			signupRequest.getUsername(),
			signupRequest.getNickname(),
			signupRequest.getAge()
		);

		userDomainService.createUser(newUser);

		String bearToken = jwtUtil.createToken(
			newUser.getId(),
			newUser.getEmail(),
			newUser.getRole(),
			newUser.getUsername(),newUser.getNickname(),
			newUser.getTier());

		return SignupResponse.from(bearToken);
	}

	/*
	이메일 로그인
	- 가입된 이메일인지 검증
	- 비밀번호가 맞는지 체크
	- 토큰 발급
	 */
	@Transactional
	public SigninResponse signin(@Valid SigninRequest signinRequest) {

    	User loginUser = userDomainService.getUser(signinRequest.getEmail());

		userDomainService.isDeletedUser(loginUser);

		//OAuth 가입 유저는 일반 로그인 불가능(향후 이메일과 소셜 모두 가입되어있는 회원의 경우 로그인 가능할 수 있도록 리팩토링)
		if (!loginUser.getAuthType().equals(AuthType.EMAIL)) {
			throw new AuthException(AuthExceptionCode.AUTH_TYPE_MISMATCH);
		}

		userDomainService.userPasswordCheck(signinRequest.getEmail(), signinRequest.getPassword());

		log.info("비밀번호 체크 완료");

		String bearToken = jwtUtil.createToken(
			loginUser.getId(),
			loginUser.getEmail(),
			loginUser.getRole(),
			loginUser.getUsername(),
			loginUser.getNickname(),
			loginUser.getTier());

		log.info("토큰 발급 완료");

		//refresh 토큰 발급
		String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
		log.info("refresh token 발급 완료");

		//redis에 RefreshToken : {} 형식으로 저장
		redisTemplate.opsForValue().set(
			"RefreshToken:" + loginUser.getId(),
			refreshToken,
			jwtUtil.getExpiration(refreshToken),
			TimeUnit.MILLISECONDS);

		return SigninResponse.from(bearToken, refreshToken);
	}

	public LogoutResponse logout(Long userId, HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER);
		}

		String token = jwtUtil.substringToken(bearerToken);

		Long expiration = jwtUtil.getRemainingTime(token);

		// Redis 실패 시에도 로그아웃 성공으로 처리 (보안상 사용자에게 노출하지 않음!)
		try {
			//블랙리스트로 등록하기
			redisTemplate.opsForValue().set(
				"LOGOUT:" + token,
				"blacklisted",
				expiration,
				TimeUnit.MILLISECONDS
			);

			redisTemplate.delete("RefreshToken:" + userId);
			} catch (Exception e) {
				log.error("Redis 오류로 인한 로그아웃 실패", e);}

		return new LogoutResponse("로그아웃 성공");
	}

	//토큰 재발급
	public RefreshTokenResponse refreshToken(HttpServletRequest request) {
		String bearToken = request.getHeader("Authorization");

		if (bearToken == null || !bearToken.startsWith("Bearer ")) {
			throw new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER);
		}
		String refreshToken = jwtUtil.substringToken(bearToken);

		if (!jwtUtil.validateToken(refreshToken)){
			throw new AuthException(AuthExceptionCode.INVALID_REFRESH_TOKEN);
		}

		Long userId = jwtUtil.getUserId(refreshToken);

		String savedToken = redisTemplate.opsForValue().get("RefreshToken:" + userId);

		if (savedToken==null || !savedToken.equals(refreshToken)){
			throw new AuthException(AuthExceptionCode.INVALID_REFRESH_TOKEN);
		}

		User user = userDomainService.getUserById(userId);

		String newAccessToken = jwtUtil.createToken(
			user.getId(),
			user.getEmail(),
			user.getRole(),
			user.getUsername(),
			user.getNickname(),
			user.getTier()
		);

		return RefreshTokenResponse.from(newAccessToken);
	}
}
