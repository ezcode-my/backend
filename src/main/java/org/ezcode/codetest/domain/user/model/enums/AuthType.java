package org.ezcode.codetest.domain.user.model;

import java.util.Arrays;

public enum AuthType {
	GOOGLE, GITHUB, EMAIL;

	public static AuthType from(String authType) {
		return Arrays.stream(AuthType.values())
			.filter(r -> r.name().equalsIgnoreCase(authType))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid authType input : " + authType));
	}


}
