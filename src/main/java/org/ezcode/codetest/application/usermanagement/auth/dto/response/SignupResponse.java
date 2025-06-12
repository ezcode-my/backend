package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignupResponse(
	@Schema(description = "생성된 token")
	String token
) {
	public static SignupResponse from(String token) {
		return new SignupResponse(token);
	}
}
