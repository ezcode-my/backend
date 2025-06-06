package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.auth.dto.signin.RefreshTokenResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.service.AuthService;
import org.ezcode.codetest.application.usermanagement.user.dto.LogoutResponse;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest));
	}

	@PostMapping("/signin")
	public ResponseEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
		log.info("signin에 정보전달");
		return ResponseEntity.status(HttpStatus.OK).body(authService.signin(signinRequest));
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(
			@AuthenticationPrincipal AuthUser authUser,
			HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.logout(authUser.getId(), request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshTokenResponse> refresh(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(request));
	}
}
