package org.ezcode.codetest.application.usermanagement.user.dto.response;

import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleUserInfoResponse {
	private final Long userId;
	private final String nickname;
	private final Tier tier;
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
