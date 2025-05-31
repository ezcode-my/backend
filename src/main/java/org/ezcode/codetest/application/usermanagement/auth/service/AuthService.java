package org.ezcode.codetest.application.usermanagement.auth.service;

import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtil;
import org.ezcode.codetest.infrastructure.security.jwt.PasswordEncoder;
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

	@Transactional
	public SignupResponse signup(SignupRequest signupRequest) {
		if (!userDomainService.existUser(signupRequest.getEmail())){
			throw new AuthException("이미 가입된 계정입니다");
		}
		log.info("서비스 레이어 진입, 이메일 검증 완료");

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
			newUser.getUsername(),newUser.getNickname());

		return SignupResponse.from(bearToken);
	}

	@Transactional
	public SigninResponse signin(@Valid SigninRequest signinRequest) {

		User loginUser = userDomainService.findUser(signinRequest.getEmail());

		userDomainService.userPasswordCheck(signinRequest.getEmail(), loginUser.getPassword());

		String bearToken = jwtUtil.createToken(
			loginUser.getId(),
			loginUser.getEmail(),
			loginUser.getRole(),
			loginUser.getUsername(),
			loginUser.getNickname());

		return SigninResponse.from(bearToken);
	}
}
