package org.ezcode.codetest.application.usermanagement.user.dto.response;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "회원 정보 응답 DTO")
public class UserInfoResponse {
	@Schema(description = "사용자 이름", example = "홍길동")
	private final String username;

	@Schema(description = "사용자 이메일", example = "user@example.com")
	private final String email;

	@Schema(description = "나이", example = "25")
	private final Integer age;

	@Schema(description = "닉네임", example = "길동이짱")
	private final String nickname;

	@Schema(description = "사용자 역할", example = "USER")
	private final UserRole userRole;

	@Schema(description = "GitHub URL", example = "https://github.com/example")
	private final String githubUrl;

	@Schema(description = "블로그 URL", example = "https://blog.example.com")
	private final String blogUrl;

	@Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/image.jpg")
	private final String profileImageUrl;

	@Schema(description = "자기소개", example = "안녕하세요, 백엔드 개발자입니다.")
	private final String introduction;

	@Schema(description = "사용자 티어", example = "GOLD")
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
