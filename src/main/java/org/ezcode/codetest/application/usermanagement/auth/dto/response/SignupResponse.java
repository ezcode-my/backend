package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignupResponse(
	@Schema(description = "회원 가입 완료 메세지")
	String message,
	@Schema(description = "생성된 accessToken")
	String accessToken,
	@Schema(description = "생성된 refreshToken")
	String refreshToken
) {
	public static SignupResponse from(String message, String accessToken, String refreshToken) {
		return new SignupResponse(message, accessToken, refreshToken);
	}
}
