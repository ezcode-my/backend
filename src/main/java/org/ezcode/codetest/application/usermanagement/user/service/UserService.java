package org.ezcode.codetest.application.usermanagement.user.service;

import org.ezcode.codetest.application.usermanagement.user.dto.UpdateUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.UserInfoResponse;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDomainService userDomainService;

	@Transactional
	public UserInfoResponse getUserInfo(AuthUser authUser) {
		log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());
		User user = userDomainService.findUser(authUser.getEmail());

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
	public UserInfoResponse updateUserInfo(AuthUser authUser, UpdateUserInfoRequest updateUserInfoRequest) {
		User user = userDomainService.findUser(authUser.getEmail());

		user.updateUserInfo(
			updateUserInfoRequest.nickname(),
			updateUserInfoRequest.githubUrl(),
			updateUserInfoRequest.blogUrl(),
			updateUserInfoRequest.profileImageUrl(),
			updateUserInfoRequest.introduction());




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
