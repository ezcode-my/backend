package org.ezcode.codetest.domain.user.model.entity;

import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import lombok.Getter;

@Getter
public class AuthUser {
	private final Long id;
	private final String email;
	private final String username;
	private final String nickname;
	private final UserRole role;
	private final Tier tier;

	public AuthUser(Long id, String username, String nickname, String email, UserRole role, Tier tier) {
		this.id = id;
		this.username = username;
		this.nickname = nickname;
		this.email = email;
		this.role = role;
		this.tier = tier;
	}
}
