package org.ezcode.codetest.application.usermanagement.user.dto;

import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {
	private String username;
	private String email;
	private Integer age;
	private String nickname;
	private UserRole userRole;
	private String githubUrl;
	private String blogUrl;
	private String profileImageUrl;
	private String introduction;
	private Tier tier;

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
}
