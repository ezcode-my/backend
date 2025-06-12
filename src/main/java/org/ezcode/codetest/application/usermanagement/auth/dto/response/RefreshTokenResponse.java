package org.ezcode.codetest.application.usermanagement.auth.dto.response;

public record RefreshTokenResponse (String token) {
	public static RefreshTokenResponse from(String token) {
		return new RefreshTokenResponse(token);
	}
}
