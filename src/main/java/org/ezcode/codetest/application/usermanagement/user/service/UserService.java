package org.ezcode.codetest.application.usermanagement.user.service;

import org.ezcode.codetest.application.usermanagement.user.dto.request.ChangeUserPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserInfoResponse;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDomainService userDomainService;

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(AuthUser authUser) {
		log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());
		User user = userDomainService.getUserById(authUser.getId());

		return UserInfoResponse.builder()
			.username(user.getUsername())
			.age(user.getAge())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImageUrl())
			.blogUrl(user.getBlogUrl())
			.introduction(user.getIntroduction())
			.nickname(user.getNickname())
			.githubUrl(user.getGithubUrl())
			.userRole(user.getRole())
			.tier(user.getTier())
			.build();
	}

	@Transactional
	public UserInfoResponse modifyUserInfo(AuthUser authUser, ModifyUserInfoRequest modifyUserInfoRequest) {
		User user = userDomainService.getUserById(authUser.getId());

		user.modifyUserInfo(
			modifyUserInfoRequest.nickname(),
			modifyUserInfoRequest.githubUrl(),
			modifyUserInfoRequest.blogUrl(),
			modifyUserInfoRequest.profileImageUrl(),
			modifyUserInfoRequest.introduction(),
			modifyUserInfoRequest.age());


		return UserInfoResponse.builder()
			.username(user.getUsername())
			.age(user.getAge())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImageUrl())
			.blogUrl(user.getBlogUrl())
			.introduction(user.getIntroduction())
			.nickname(user.getNickname())
			.githubUrl(user.getGithubUrl())
			.userRole(user.getRole())
			.tier(user.getTier())
			.build();
	}

	@Transactional
	public ChangeUserPasswordResponse modifyUserPassword(AuthUser authUser, ChangeUserPasswordRequest changeUserPasswordRequest) {
		User user = userDomainService.getUserById(authUser.getId());

		//소셜로그인 회원은 변경 불가
		if (!user.getAuthType().equals(AuthType.EMAIL)) {
			throw new AuthException(AuthExceptionCode.NOT_EMAIL_USER);
		}

		//기존 비밀번호와 새로운 비밀번호가 같은지 확인 -> 기존과 같으면 변경 불가
		userDomainService.passwordComparison(changeUserPasswordRequest.newPassword(), user.getPassword());

		String newPassword = userDomainService.encodePassword(changeUserPasswordRequest.newPassword());

		user.modifyPassword(newPassword);

		return new ChangeUserPasswordResponse("비밀번호를 성공적으로 변경했습니다");
	}
}
