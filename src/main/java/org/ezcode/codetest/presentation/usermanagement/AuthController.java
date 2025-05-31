package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signin.SigninResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.signup.SignupResponse;
import org.ezcode.codetest.application.usermanagement.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
		return ResponseEntity.status(HttpStatus.OK).body(authService.signin(signinRequest));
	}
}
