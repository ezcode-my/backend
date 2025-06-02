package org.ezcode.codetest.application.usermanagement.auth.service;

import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.port.JwtUtil;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
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

		return SigninResponse.from(bearToken);
	}

	public String logout(Long userId, HttpServletRequest request) {

		return "로그아웃 성공";
	}
}
