package org.ezcode.codetest.application.usermanagement.user.service;

import org.ezcode.codetest.application.usermanagement.user.dto.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.UserInfoResponse;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
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
}
