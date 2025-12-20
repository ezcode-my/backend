package org.ezcode.codetest.domain.user.model.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	private Integer age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Column(name = "github_url")
	private String githubUrl;

	@Column(name = "blog_url")
	private String blogUrl;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	private String introduction;

	@Enumerated(EnumType.STRING)
	private Tier tier;

	@Column(name = "review_token")
	private int reviewToken;

	private boolean isDeleted;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserAuthType> userAuthTypes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id")
	private Language language;

	private boolean verified; //이메일 인증 여부

	private boolean gitPushStatus; //깃허브 자동 push 여부

	/*
	처음 유저 생성(가입) 시에는 기본 정보만 받음
	- 이메일, 비번, 이름, 별명, 나이
	-> 이후 회원정보 업데이트할 때, 원하는 정보를 입력할 수 있도록 함
	 */
	public static User emailUser(String email, String password, String username, String nickname, Integer age, Language language) {
		return User.builder()
			.email(email)
			.password(password)
			.username(username)
			.nickname(nickname)
			.age(age)
			.tier(Tier.NEWBIE)
			.role(UserRole.USER)
			.isDeleted(false)
			.verified(false)
			.gitPushStatus(false)
			.language(language)
			.build();
	}

	/*
	OAuth2로 로그인한 유저 저장
	구글 이외의 다른 소셜 로그인 확장 가능성을 고려해 socialUser 이름 유지
	 */
	public static User socialUser(String email, String username, String nickname, String password, Language language) {
		return User.builder()
			.email(email)
			.username(username)
			.role(UserRole.USER)
			.tier(Tier.NEWBIE)
			.nickname(nickname) //닉네임 자동 생성
			.password(password)
			.isDeleted(false)
			.verified(false)
			.gitPushStatus(false)
			.language(language)
			.build();
	}

	//깃허브 아이디와 url을 함께 저장하기 위해 따로 저장
	public static User githubUser(String email, String username, String nickname, String password, String githubUrl, Language language){
		return User.builder()
			.email(email)
			.username(username)
			.role(UserRole.USER)
			.tier(Tier.NEWBIE)
			.nickname(nickname) //닉네임 자동 생성
			.password(password)
			.isDeleted(false)
			.verified(false)
			.githubUrl(githubUrl)
			.gitPushStatus(false)
			.language(language)
			.build();
	}


	@Builder
	public User(String email, String password, String username, String nickname,
		Integer age, Tier tier, UserRole role, boolean isDeleted, boolean verified, String githubUrl, boolean gitPushStatus, Language language) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.nickname = nickname;
		this.age = age;
		this.tier = tier;
		this.role = role;
		this.isDeleted = isDeleted;
		this.verified = verified;
		this.githubUrl = githubUrl;
		this.gitPushStatus = gitPushStatus;
		this.language = language;
	}

	/*
	유저 정보 업데이트
	- 만약 입력 값이 없다면, 기존 값 유지
	 */
	public void modifyUserInfo(String nickname, String githubUrl, String blogUrl, String introduction, Integer age, Language language) {
		this.nickname = (nickname == null || nickname.isBlank()) ? this.nickname : nickname;
		this.githubUrl = (githubUrl == null || githubUrl.isBlank()) ? this.githubUrl : githubUrl;
		this.blogUrl = (blogUrl == null || blogUrl.isBlank()) ? this.blogUrl : blogUrl;
		this.introduction = (introduction == null || introduction.isBlank()) ? this.introduction : introduction;
		this.age = (age == null) ? this.age : age;
		this.language = (language == null) ? this.language : language;
	}


	/**
	 * 회원 탈퇴 시 호출되는 메서드
	 * - isDeleted 플래그를 true로 설정
	 * - 이메일을 변경하여 동일 이메일 재가입 시 unique key 충돌 방지
	 *   (예: user@example.com -> user@example.com__deleted_20231220)
	 */
	public void markAsDeleted() {
		this.isDeleted = true;
		if (this.email != null && !this.email.isBlank()) {
			String deletedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			this.email = this.email + "__deleted_" + deletedDate;
		}
	}

	public boolean shouldSkipNotification(User recipient) {

		return recipient == null || this.getId().equals(recipient.getId());
	}

	public void modifyPassword(String newPassword) {
		this.password = newPassword;
	}

	public void setVerified(){
		this.verified = true;
	}

	public void setGithubUrl(String githubUrl){
		this.githubUrl = githubUrl;
	}

	public void decreaseReviewToken() {
		this.reviewToken -= 1;
	}

	public void setGitPushStatus(boolean gitPushStatus) {
		this.gitPushStatus = gitPushStatus;
	}

	public boolean getGitPushStatus() {
		return gitPushStatus;
	}

    public void modifyProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
    }

	public void modifyUserRole(UserRole userRole) {
		this.role = userRole;
	}

	public void setLanguage(Language userLanguage) {
		this.language = userLanguage;
	}

	public static final int DEFAULT_REVIEW_TOKEN = 20;
	public static final int FULL_WEEK_REVIEW_TOKEN = 40;

	/**
	 * 기본 리뷰 토큰을 설정 이메일 인증 직후, OAuth 가입 시 사용)
	 */
	public void setDefaultReviewToken() {
		this.reviewToken = DEFAULT_REVIEW_TOKEN;
	}

	/**
	 * 주간 풀 참여 리뷰 토큰을 설정 (주간 스케줄러에서 사용)
	 */
	public void setFullWeekReviewToken() {
		this.reviewToken = FULL_WEEK_REVIEW_TOKEN;
	}
}
