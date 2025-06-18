package org.ezcode.codetest.application.usermanagement.auth.service;

import java.util.List;
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
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


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

		User existUser = userDomainService.getUserByEmail(signupRequest.getEmail());

		String bearToken;

		//만약 아예 유저 테이블에 없으면 둘 다 저장
		if (existUser == null) {
			User newUser = User.emailUser(
				signupRequest.getEmail(),
				encodedPassword,
				signupRequest.getUsername(),
				signupRequest.getNickname(),
				signupRequest.getAge()
			);
			UserAuthType userAuthType = new UserAuthType(newUser, AuthType.EMAIL);
			userDomainService.createUser(newUser);
			userDomainService.createUserAuthType(userAuthType);

			bearToken = jwtUtil.createToken(
				newUser.getId(),
				newUser.getEmail(),
				newUser.getRole(),
				newUser.getUsername(),newUser.getNickname(),
				newUser.getTier());
		} else {
			//유저 테이블에는 존재하다면 AuthType만 추가
			UserAuthType userAuthType = new UserAuthType(existUser, AuthType.EMAIL);
			userDomainService.createUserAuthType(userAuthType);

			//로컬 가입(이메일)은 안되어있는데 소셜은 되어있는 경우이므로, UUID 비번을 사용자가 지정한 비번으로 변경한다. -> 이후 비번 변경하면 User테이블에서 변경하면됨.
			existUser.modifyPassword(encodedPassword);
			log.info("유저 타입 저장 완료 {}", userAuthType);

			bearToken = jwtUtil.createToken(
				existUser.getId(),
				existUser.getEmail(),
				existUser.getRole(),
				existUser.getUsername(),
				existUser.getNickname(),
				existUser.getTier());
		}

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
		List<AuthType> userAuthTypes = userDomainService.getUserAuthTypes(loginUser);

		//OAuth 가입 유저는 일반 로그인 불가능(향후 이메일과 소셜 모두 가입되어있는 회원의 경우 로그인 가능할 수 있도록 리팩토링)
		if (!userAuthTypes.contains(AuthType.EMAIL)) {
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


	public LogoutResponse logout(Long userId, String token) {

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
	public RefreshTokenResponse refreshToken(String token) {
		log.info("서비스 입장");

		Long userId = jwtUtil.getUserId(token);

		log.info("유저 아이디 가져옴 id : {}", userId);
		String savedToken = redisTemplate.opsForValue().get("RefreshToken:" + userId);
		log.info("저장된 토큰 가져옴 {}", savedToken);
		if (savedToken==null || !savedToken.equals(token)){
			log.error("저장된 토큰 없음");
			throw new AuthException(AuthExceptionCode.INVALID_REFRESH_TOKEN);
		}

		User user = userDomainService.getUserById(userId);
		log.info("유저 도메인서비스에서 유저 아이디로 유저 찾아옴");
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
