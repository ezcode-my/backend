package org.ezcode.codetest.application.usermanagement.user.dto;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {
	private final String username;
	private final String email;
	private final Integer age;
	private final String nickname;
	private final UserRole userRole;
	private final String githubUrl;
	private final String blogUrl;
	private final String profileImageUrl;
	private final String introduction;
	private final Tier tier;

	@Builder
	public UserInfoResponse(String username, String email, String nickname, UserRole userRole, Tier tier,
		Integer age, String githubUrl, String blogUrl, String profileImageUrl, String introduction) {
		this.username = username;
		this.email = email;
		this.nickname = nickname;
		this.age = age;
		this.githubUrl = githubUrl;
		this.blogUrl = blogUrl;
		this.profileImageUrl = profileImageUrl;
		this.introduction = introduction;
		this.tier = tier;
		this.userRole = userRole;
	}

	public static UserInfoResponse fromEntity(User user) {
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
