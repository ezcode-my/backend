package org.ezcode.codetest.common.dto;

import org.ezcode.codetest.domain.user.model.enums.UserRole;

import lombok.Getter;

@Getter
public class AuthUser {
	private final Long id;
	private final String email;
	private final String username;
	private final String nickname;
	private final UserRole userRole;

	public AuthUser(Long id, String email, String username, String nickname, UserRole userRole) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.nickname = nickname;
		this.userRole = userRole;
	}
}
