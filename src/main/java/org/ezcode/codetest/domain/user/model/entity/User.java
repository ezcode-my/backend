package org.ezcode.codetest.domain.user.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = false)
	private Integer age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_type", nullable = false)
	private AuthType authType;

	@Column(name = "github_url")
	private String githubUrl;

	@Column(name = "blog_url")
	private String blogUrl;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	private String introduction;

	@Enumerated(EnumType.STRING)
	private Tier tier;

	private boolean isDeleted;


	/*
	처음 유저 생성(가입) 시에는 기본 정보만 받음
	- 이메일, 비번, 이름, 별명, 나이
	-> 이후 회원정보 업데이트할 때, 원하는 정보를 입력할 수 있도록 함
	 */
	@Builder
	public User(String email, String password, String username, String nickname, Integer age){
		this.email = email;
		this.password = password;
		this.username = username;
		this.nickname = nickname;
		this.age = age;
		this.authType = AuthType.EMAIL;
		this.tier = Tier.NEWBIE;
		this.role = UserRole.USER;
		this.isDeleted = false;
	}

	/*
	유저 정보 업데이트
	- 만약 입력 값이 없다면, 기존 값 유지
	 */
	public void modifyUserInfo(String nickname, String githubUrl, String blogUrl, String profileImageUrl, String introduction){
		this.nickname = (nickname == null || nickname.isBlank()) ? this.nickname : nickname;
		this.githubUrl = (githubUrl == null || githubUrl.isBlank()) ? this.githubUrl : githubUrl;
		this.blogUrl = (blogUrl == null || blogUrl.isBlank()) ? this.blogUrl : blogUrl;
		this.profileImageUrl = (profileImageUrl == null || profileImageUrl.isBlank()) ? this.profileImageUrl : profileImageUrl;
		this.introduction = (introduction == null || introduction.isBlank()) ? this.introduction : introduction;
	}


	public void setDeleted() {
		this.isDeleted = true;
	}


}
