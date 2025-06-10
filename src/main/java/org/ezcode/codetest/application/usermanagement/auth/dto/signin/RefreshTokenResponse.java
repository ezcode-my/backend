package org.ezcode.codetest.application.usermanagement.auth.dto.signin;

public record RefreshTokenResponse (String token) {
	public static RefreshTokenResponse from(String token) {
		return new RefreshTokenResponse(token);
	}
}
