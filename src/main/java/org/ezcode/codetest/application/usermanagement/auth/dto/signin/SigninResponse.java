package org.ezcode.codetest.application.usermanagement.auth.dto.signin;


public record SigninResponse(String accessToken, String refreshToken)  {
	public static SigninResponse from(String accessToken, String refreshToken) {
		return new SigninResponse(accessToken, refreshToken);
	}
}
