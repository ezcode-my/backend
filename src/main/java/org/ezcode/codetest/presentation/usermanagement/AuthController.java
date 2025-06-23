package org.ezcode.codetest.presentation.usermanagement;

import java.util.Optional;

import org.ezcode.codetest.application.usermanagement.auth.dto.request.FindPasswordRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.VerifyEmailCodeRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.FindPasswordResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.RefreshTokenResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SendEmailCodeResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.VerifyEmailCodeResponse;
import org.ezcode.codetest.application.usermanagement.auth.service.AuthService;
import org.ezcode.codetest.application.usermanagement.user.dto.response.LogoutResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "인증/인가", description = "회원가입, 로그인, 로그아웃, 토큰 재발급 관련 API")
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "회원가입", description = "이메일, 비밀번호 등 정보를 입력받아 회원가입을 진행합니다.")
	@PostMapping("/auth/signup")
	public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest));
	}

	@Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 토큰을 발급받습니다.")
	@PostMapping("/auth/signin")
	public ResponseEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.signin(signinRequest));
	}

	@Operation(summary = "로그아웃", description = "현재 로그인된 사용자의 로그아웃을 수행합니다.")
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(
			@AuthenticationPrincipal AuthUser authUser,
			HttpServletRequest request) {

		String token = Optional.ofNullable(request.getHeader("Authorization"))
			.map(h -> h.replace("Bearer ", ""))
			.orElseThrow(()-> new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER));

		return ResponseEntity.status(HttpStatus.OK).body(authService.logout(authUser.getId(), token));
	}

	@Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.",
		security = @SecurityRequirement(name = "JWT_REFRESH")
	)
	@PostMapping("/auth/refresh")
	public ResponseEntity<RefreshTokenResponse> refresh(HttpServletRequest request) {

		String token = Optional.ofNullable(request.getHeader("JWT_REFRESH"))
			.map(h -> h.replace("Bearer ", ""))
			.orElseThrow(()-> new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER));

		log.info("Refresh token 추출 : {}", token);
		return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(token));
	}

	@Operation(summary = "이메일 인증 코드 전송", description = "현재 로그인된 회원의 이메일로 인증 코드를 전송합니다.")
	@PostMapping("/email/send")
	public ResponseEntity<SendEmailCodeResponse> sendMailCode(
		@AuthenticationPrincipal AuthUser authUser
	){
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.sendEmailCode(authUser.getId(), authUser.getEmail()));
	}

	//이메일에서 버튼 클릭하면 자동으로 연결
	@Operation(summary = "이메일 코드 입력 및 인증", description = "이메일로 받은 코드를 입력하여 이메일 인증된 회원으로 전환합니다")
	@GetMapping("/auth/verify")
	public ResponseEntity<VerifyEmailCodeResponse> verifyEmailCode(
		@RequestParam String email,
		@RequestParam String key
	){
		return ResponseEntity.status(HttpStatus.OK).body(authService.verifyEmailCode(email, key));
	}


	@PostMapping("/auth/find-password")
	public ResponseEntity<FindPasswordResponse> findPassword(
		@RequestBody FindPasswordRequest request
	){
		return ResponseEntity.status(HttpStatus.OK).body(authService.findPassword(request));
	}

	@GetMapping("/auth/verify-password-code")
	public ResponseEntity<FindPasswordResponse> changePasswordByEmail(
		@RequestParam String email,
		@RequestParam String key
	){
		return ResponseEntity.status(HttpStatus.OK).body(authService.changePasswordByEmail(email, key));
	}
}
