package org.ezcode.codetest.domain.user.model.entity;

import java.util.Collection;
import java.util.List;

import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class AuthUser implements UserDetails {
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() { return ""; }

}
