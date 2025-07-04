package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.user.dto.request.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ChangeUserPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.GrantAdminRoleResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserInfoResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserProfileImageResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserReviewTokenResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.WithdrawUserResponse;
import org.ezcode.codetest.application.usermanagement.user.service.UserService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 기본 기능", description = "사용자 정보 조회, 수정, 비밀번호 변경, 회원 탈퇴 API")
public class UserController {
	private final UserService userService;

	@Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
	@GetMapping("/users")
	public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal AuthUser authUser){
		log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(authUser));
	}

	@Operation(summary = "내 정보 수정", description = "닉네임, 블로그, 깃허브, 소개 등 개인 정보를 추가하거나 수정합니다.")
	@PutMapping("/users")
	public ResponseEntity<UserInfoResponse> modifyUserInfo(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody ModifyUserInfoRequest modifyUserInfoRequest
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.modifyUserInfo(authUser, modifyUserInfoRequest));
	}

	//유저 프로필 이미지 등록
	@Operation(
		summary = "프로필 이미지 등록",
		description = "유저의 프로필 이미지를 등록합니다. 기존의 이미지가 있는 경우, 기존 이미지가 삭제되고 새로운 이미지로 교체됩니다.")
	@PutMapping("/users/profile")
	public ResponseEntity<UserProfileImageResponse> uploadUserProfileImage(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestPart(value = "image", required = false) MultipartFile image
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.uploadUserProfileImage(authUser, image));
	}

	@Operation(
		summary = "프로필 이미지 삭제",
		description = "유저의 프로필 이미지를 삭제 후 기본 이미지로 대체됩니다.")
	@DeleteMapping("/users/profile")
	public ResponseEntity<UserProfileImageResponse> deleteUserProfileImage(
		@AuthenticationPrincipal AuthUser authUser
	){
	return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserProfileImage(authUser));
	}

	@Operation(summary = "비밀번호 변경", description = "기존 비밀번호와 새 비밀번호를 입력하여 비밀번호를 변경합니다.")
	@PutMapping("/users/password")
	public ResponseEntity<ChangeUserPasswordResponse> modifyUserPassword(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody ChangeUserPasswordRequest changeUserPasswordRequest
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.modifyUserPassword(authUser, changeUserPasswordRequest));
	}

	@Operation(summary = "회원 탈퇴", description = "현재 로그인된 사용자를 탈퇴 처리합니다.")
	@DeleteMapping("/users/withdraw")
	public ResponseEntity<WithdrawUserResponse> withdraw(
		@AuthenticationPrincipal AuthUser authUser
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.withdrawUser(authUser));
	}

	@Operation(summary = "회원 AI 리뷰 토큰 조회", description = "회원의 남은 AI리뷰용 토큰의 개수를 조회합니다.")
	@GetMapping("/users/review-token")
	public ResponseEntity<UserReviewTokenResponse> getReviewToken(
		@AuthenticationPrincipal AuthUser authUser
	){
		return ResponseEntity.status(HttpStatus.OK).body(userService.getReivewToken(authUser));
	}

}
