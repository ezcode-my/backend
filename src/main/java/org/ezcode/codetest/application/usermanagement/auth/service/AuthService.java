package org.ezcode.codetest.application.usermanagement.auth.service;

import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.port.JwtUtil;
import org.ezcode.codetest.application.usermanagement.user.dto.LogoutResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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
	이메일 로그인
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

		User newUser = User.builder()
			.email(signupRequest.getEmail())
			.password(encodedPassword)
			.username(signupRequest.getUsername())
			.nickname(signupRequest.getNickname())
			.age(signupRequest.getAge())
			.build();

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

		userDomainService.userPasswordCheck(signinRequest.getEmail(), signinRequest.getPassword());

		String bearToken = jwtUtil.createToken(
			loginUser.getId(),
			loginUser.getEmail(),
			loginUser.getRole(),
			loginUser.getUsername(),
			loginUser.getNickname(),
			loginUser.getTier());

		String redisToken = jwtUtil.substringToken(bearToken);
		redisTemplate.opsForValue().set(
			"LOGIN:" + loginUser.getId(),
			redisToken,
			jwtUtil.getExpiration(redisToken),
			TimeUnit.MILLISECONDS);

		return SigninResponse.from(bearToken);
	}

	public LogoutResponse logout(Long userId, HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER);
		}

		String token = jwtUtil.substringToken(bearerToken);

		Long expiration = jwtUtil.getRemainingTime(token);

		// Redis 실패 시에도 로그아웃 성공으로 처리 (보안상 사용자에게 노출하지 않음)
		try {
			//블랙리스트로 등록하기
			redisTemplate.opsForValue().set(
				"LOGOUT:" + token,
				"blacklisted",
				expiration,
				TimeUnit.MILLISECONDS
			);

			redisTemplate.delete("LOGIN:" + userId);
			} catch (Exception e) {
				log.error("Redis 오류로 인한 로그아웃 처리 실패", e);}

		return new LogoutResponse("success");
	}
}
