package org.ezcode.codetest.domain.user.model;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum UserRole {
	USER, ADMIN, PRO;

	public static UserRole from(String userRole) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(userRole))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid userRole : " + userRole));
	}
}
