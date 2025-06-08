package org.ezcode.codetest.domain.user.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.usermanagement.user.dto.OAuth2Response;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
	private final OAuth2Response oAuth2Response;
	private final String userRole;

	@Override
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userRole;
			}
		});
		return authorities;
	}

	@Override
	public String getName() {
		return oAuth2Response.getName();
	}

	public String getEmail() {
		return oAuth2Response.getEmail();
	}

	public String getProvider(){
		return oAuth2Response.getProvider();
	}

	public UserRole getUserRole() {
		return UserRole.from(userRole);
	}

	public String getUsername() {
		return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
	}
}
