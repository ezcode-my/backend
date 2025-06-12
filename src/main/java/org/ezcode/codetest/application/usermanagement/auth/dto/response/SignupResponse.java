package org.ezcode.codetest.application.usermanagement.auth.dto.response;


public record SignupResponse(String token) {
	public static SignupResponse from(String token) {
		return new SignupResponse(token);
	}
}
