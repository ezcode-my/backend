package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.user.dto.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.UserInfoResponse;
import org.ezcode.codetest.application.usermanagement.user.service.UserService;
import org.ezcode.codetest.common.annotation.Auth;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
		@Auth AuthUser authUser,
		@RequestBody ModifyUserInfoRequest modifyUserInfoRequest
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.modifyUserInfo(authUser, modifyUserInfoRequest));
	}
}
