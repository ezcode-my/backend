package org.ezcode.codetest.application.usermanagement.auth.dto.response;

public record OAuthResponse(
	String accessToken,
	String refreshToken
){
	public static OAuthResponse from(String accessToken, String refreshToken) {
		return new OAuthResponse(accessToken, refreshToken);
	}
}
