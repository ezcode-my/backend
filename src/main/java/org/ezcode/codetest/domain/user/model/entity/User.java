package org.ezcode.codetest.domain.user.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

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
	public static User emailUser(String email, String password, String username, String nickname, Integer age){
		return User.builder()
			.email(email)
			.password(password)
			.username(username)
			.nickname(nickname)
			.age(age)
			.authType(AuthType.EMAIL)
			.tier(Tier.NEWBIE)
			.role(UserRole.ADMIN) // 테스트용
			.isDeleted(false)
			.build();
	}

	/*
	OAuth2로 로그인한 유저 저장
	 */
	public static User googleUser(String email, String username){
		return User.builder()
			.email(email)
			.username(username)
			.authType(AuthType.GOOGLE)
			.nickname("user_" + UUID.randomUUID().toString().split("-")[0])//닉네임은 자동으로 생성해주고, 나중에 수정할 수 있도록 함
			.tier(Tier.NEWBIE)
			.role(UserRole.USER)
			.isDeleted(false)
			.password(UUID.randomUUID().toString())
			.build();
	}

	public static User githubUser(String email, String username){
		return User.builder()
			.email(email)
			.username(username)
			.authType(AuthType.GITHUB)
			.nickname("user_" + UUID.randomUUID().toString().split("-")[0])//닉네임은 자동으로 생성해주고, 나중에 수정할 수 있도록 함
			.tier(Tier.NEWBIE)
			.role(UserRole.USER)
			.password(UUID.randomUUID().toString())
			.isDeleted(false)
			.build();
	}

	@Builder
	public User(String email, String password, String username, String nickname,
		Integer age, AuthType authType, Tier tier, UserRole role, boolean isDeleted) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.nickname = nickname;
		this.age = age;
		this.authType = authType;
		this.tier = tier;
		this.role = role;
		this.isDeleted = isDeleted;
	}

	/*
	유저 정보 업데이트
	- 만약 입력 값이 없다면, 기존 값 유지
	 */
	public void modifyUserInfo(String nickname, String githubUrl, String blogUrl, String profileImageUrl, String introduction, Integer age){
		this.nickname = (nickname == null || nickname.isBlank()) ? this.nickname : nickname;
		this.githubUrl = (githubUrl == null || githubUrl.isBlank()) ? this.githubUrl : githubUrl;
		this.blogUrl = (blogUrl == null || blogUrl.isBlank()) ? this.blogUrl : blogUrl;
		this.profileImageUrl = (profileImageUrl == null || profileImageUrl.isBlank()) ? this.profileImageUrl : profileImageUrl;
		this.introduction = (introduction == null || introduction.isBlank()) ? this.introduction : introduction;
		this.age = (age == null) ? this.age : age;
	}


	public void setDeleted() {
		this.isDeleted = true;
	}

	public boolean shouldSkipNotification(User recipient) {

		return recipient == null || this.getId().equals(recipient.getId());
	}

	public void modifyPassword(String newPassword) {
		this.password = newPassword;
	}
}
