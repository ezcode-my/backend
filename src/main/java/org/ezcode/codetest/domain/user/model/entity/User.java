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
	}

	// @Builder
	// public User(String username, String email, String password, String nickname, Integer age,
	// 	String githubUrl, String blogUrl, String profileImageUrl, String introduction) {
	// 	this.username = username;
	// 	this.email = email;
	// 	this.password = password;
	// 	this.nickname = nickname;
	// 	this.age = age;
	// 	this.role = UserRole.USER;
	// 	this.authType = AuthType.EMAIL;
	// 	this.githubUrl = githubUrl;
	// 	this.blogUrl = blogUrl;
	// 	this.profileImageUrl = profileImageUrl;
	// 	this.introduction = introduction;
	// 	this.tier = Tier.NEWBIE;
	// 	this.isDeleted = false;
	// }

	public void setDeleted(boolean deleted) {
		this.isDeleted = true;
	}


}
