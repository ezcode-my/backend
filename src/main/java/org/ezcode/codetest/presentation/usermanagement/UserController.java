package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.user.dto.request.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ChangeUserPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserInfoResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.WithdrawUserResponse;
import org.ezcode.codetest.application.usermanagement.user.service.UserService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/users")
	public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal AuthUser authUser){
		log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(authUser));
	}

	@PutMapping("/users")
	public ResponseEntity<UserInfoResponse> modifyUserInfo(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody ModifyUserInfoRequest modifyUserInfoRequest
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.modifyUserInfo(authUser, modifyUserInfoRequest));
	}

	@PutMapping("/users/password")
	public ResponseEntity<ChangeUserPasswordResponse> modifyUserPassword(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody ChangeUserPasswordRequest changeUserPasswordRequest
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.modifyUserPassword(authUser, changeUserPasswordRequest));
	}

	@DeleteMapping("/users/withdraw")
	public ResponseEntity<WithdrawUserResponse> withdraw(
		@AuthenticationPrincipal AuthUser authUser
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.withdrawUser(authUser));

	}
}
