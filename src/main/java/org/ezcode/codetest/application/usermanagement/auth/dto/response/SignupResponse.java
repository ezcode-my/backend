package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignupResponse(
	@Schema(description = "회원 가입 완료 메세지")
	String message
) {
	public static SignupResponse from(String message) {
		return new SignupResponse(message);
	}
}
