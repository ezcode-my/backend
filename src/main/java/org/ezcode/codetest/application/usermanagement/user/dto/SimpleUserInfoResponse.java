package org.ezcode.codetest.application.usermanagement.user.dto;

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
}
