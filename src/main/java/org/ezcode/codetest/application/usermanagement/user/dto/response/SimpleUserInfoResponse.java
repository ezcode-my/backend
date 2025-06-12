package org.ezcode.codetest.application.usermanagement.user.dto.response;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "간단한 사용자 정보 응답 DTO")
public class SimpleUserInfoResponse {
	@Schema(description = "사용자 ID", example = "1")
	private final Long userId;

	@Schema(description = "사용자 별명", example = "다람쥐쳇바퀴에굴러가")
	private final String nickname;

	@Schema(description = "사용자 티어", example = "GOLD")
	private final Tier tier;

	@Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile.jpg")
	private final String profileImageUrl;

	@Builder
	public SimpleUserInfoResponse(Long userId, String nickname, Tier tier, String profileImageUrl) {
		this.userId = userId;
		this.nickname = nickname;
		this.tier = tier;
		this.profileImageUrl = profileImageUrl;
	}

	public static SimpleUserInfoResponse fromEntity(User user) {
		return SimpleUserInfoResponse.builder()
			.userId(user.getId())
			.nickname(user.getNickname())
			.tier(user.getTier())
			.profileImageUrl(user.getProfileImageUrl())
			.build();
	}
}
