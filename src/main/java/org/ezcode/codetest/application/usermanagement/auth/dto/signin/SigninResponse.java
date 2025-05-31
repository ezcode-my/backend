package org.ezcode.codetest.application.usermanagement.auth.dto.signin;

public record SigninResponse(String token)  {
	public static SigninResponse from(String token) {
		return new SigninResponse(token);
	}
}
