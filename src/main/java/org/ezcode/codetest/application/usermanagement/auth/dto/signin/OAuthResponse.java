package org.ezcode.codetest.application.usermanagement.auth.dto.signin;

public record OAuthResponse(
	String accessToken,
	String refreshToken
){
	public static OAuthResponse from(String accessToken, String refreshToken) {
		return new OAuthResponse(accessToken, refreshToken);
	}
}
