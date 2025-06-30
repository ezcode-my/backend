package org.ezcode.codetest.application.usermanagement.auth.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.usermanagement.auth.dto.request.FindPasswordRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.FindPasswordResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.RefreshTokenResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SendEmailResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.VerifyEmailCodeResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ResetPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.LogoutResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.VerifyFindPasswordResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.exception.code.UserExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.service.MailService;
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
	private final MailService mailService;

	/*
	이메일 회원가입
	- 이미 가입된 이메일 거절
	- 비밀번호 암호화
	 */
	@Transactional
	public SignupResponse signup(SignupRequest request) {
		validateRequest(request);
		userRegisterationProcess(request);

		return SignupResponse.from("회원가입이 완료되었습니다.");
	}

	//1. 보낸 요청의 비밀번호&비밀번호확인이 일치하는지
	private void validateRequest(SignupRequest request) {
		userDomainService.checkEmailUnique(request.getEmail());
		if (!request.getPassword().equals(request.getPasswordConfirm())){
			throw new AuthException(AuthExceptionCode.PASSWORD_NOT_MATCH);
		};
	}

	//2. 이미 다른 방식으로 회원가입한 유저인지 검증
	private void userRegisterationProcess(SignupRequest request) {
		String encodedPassword = userDomainService.encodePassword(request.getPassword());
		User existUser = userDomainService.getUserByEmail(request.getEmail());
		if ((existUser == null)) {
			createNewUser(request, encodedPassword);
		} else {
			updateExistingUser(existUser, encodedPassword);
		}
	}

	//3. 만약 아예 첫 가입 유저일 때
	private void createNewUser(SignupRequest request, String encodedPassword) {
		User newUser = User.emailUser(
			request.getEmail(),
			encodedPassword,
			request.getUsername(),
			request.getNickname(),
			request.getAge()
		);

		userDomainService.createUser(newUser);
		userDomainService.createUserAuthType(new UserAuthType(newUser, AuthType.EMAIL));

	}

	//4. 만약 이전에 다른 방식으로 가입했었던(소셜) 회원일 때 -> UserAuthType테이블에 인증 방식만 추가
	private void updateExistingUser(User existUser, String encodedPassword) {
		//로컬 가입(이메일)은 안되어있는데 소셜은 되어있는 경우이므로, UUID 비번을 사용자가 지정한 비번으로 변경한다.
		// -> 이후 비번 변경하면 User테이블에서 변경하면됨.
		existUser.modifyPassword(encodedPassword);
		UserAuthType userAuthType = new UserAuthType(existUser, AuthType.EMAIL);
		userDomainService.createUserAuthType(userAuthType);
	}

	@Transactional
	public SendEmailResponse sendEmailCode(Long userId, String email, String redirectUrl) {
		mailService.sendButtonMail(userId, email, redirectUrl);
		return SendEmailResponse.from("인증 코드를 전송했습니다.");
	}

	@Transactional
	public VerifyEmailCodeResponse verifyEmailCode(String email, String key) {
		User user = userDomainService.getUserByEmail(email);

		boolean isMatch = mailService.verifyCode(user.getId(), key);

		if (isMatch){
			user.setVerified();
			return VerifyEmailCodeResponse.from("인증되었습니다", isMatch);
		} else {
			throw new UserException(UserExceptionCode.NOT_MATCH_CODE);
		}
	}

	private String createAccessToken(User user) {
		return jwtUtil.createAccessToken(
			user.getId(),
			user.getEmail(),
			user.getRole(),
			user.getUsername(),
			user.getNickname(),
			user.getTier());
	}
	private String createRefreshToken(User user) {
		String refreshToken = jwtUtil.createRefreshToken(user.getId());
		redisTemplate.opsForValue().set(
			"RefreshToken:" + user.getId(),
			refreshToken,
			jwtUtil.getExpiration(refreshToken),
			TimeUnit.MILLISECONDS);
		return refreshToken;
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

		String accessToken = createAccessToken(loginUser);

		//refresh 토큰 발급
		String refreshToken = createRefreshToken(loginUser);

		return SigninResponse.from(accessToken, refreshToken);
	}


	@Transactional
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
	@Transactional
	public RefreshTokenResponse refreshToken(String token) {
		log.info("서비스 입장");

		Long userId = jwtUtil.getUserId(token);

		String savedToken = redisTemplate.opsForValue().get("RefreshToken:" + userId);

		if (savedToken==null || !savedToken.equals(token)){
			throw new AuthException(AuthExceptionCode.INVALID_REFRESH_TOKEN);
		}

		User user = userDomainService.getUserById(userId);

		String newAccessToken = jwtUtil.createAccessToken(
			user.getId(),
			user.getEmail(),
			user.getRole(),
			user.getUsername(),
			user.getNickname(),
			user.getTier()
		);

		return RefreshTokenResponse.from(newAccessToken);
	}

	//비밀번호 찾기 메일 전송
	@Transactional
	public FindPasswordResponse findPassword(FindPasswordRequest request) {

		User user = userDomainService.getUserByEmail(request.getEmail());
		if(user == null || user.isDeleted()){
			throw new AuthException(AuthExceptionCode.USER_NOT_FOUND);
		}

		mailService.sendPasswordMail(user.getId(), request.getEmail(), request.getRedirectUrl());

		return FindPasswordResponse.from("이메일 전송되었습니다.");
	}

	public VerifyFindPasswordResponse verifyFindPassword(String email, String key) {

		User user = userDomainService.getUserByEmail(email);

		boolean isMatch = mailService.verifyPasswordCode(user.getId(), key);

		String tempResetToken = jwtUtil.createEmailToken(user.getId(), email);

		if (isMatch){
			user.setVerified();
			return VerifyFindPasswordResponse.from("인증되었습니다", tempResetToken);
		} else {
			throw new UserException(UserExceptionCode.NOT_MATCH_CODE);
		}
	}

	@Transactional
	public ChangeUserPasswordResponse resetPassword(@Valid ResetPasswordRequest request) {
		Long userId = jwtUtil.getUserId(request.tempResetToken());
		log.info("요청 유저 id : {}", userId);

		User user = userDomainService.getUserById(userId);
		//기존과 같은 비밀번호일때
		userDomainService.passwordComparison(request.newPassword(), user.getPassword());
		if (!request.newPassword().equals(request.newPasswordConfirm())){
			throw new AuthException(AuthExceptionCode.PASSWORD_NOT_MATCH);
		}
		String encodedPassword = userDomainService.encodePassword(request.newPassword());
		user.modifyPassword(encodedPassword);
		return new ChangeUserPasswordResponse("비밀번호 변경이 완료되었습니다.");
	}
}
