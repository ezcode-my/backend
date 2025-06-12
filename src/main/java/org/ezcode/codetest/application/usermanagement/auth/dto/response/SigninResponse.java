package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record SigninResponse(
	@Schema(description = "생성된 accessToken")
	String accessToken,
	@Schema(description = "생성된 refreshToken")
	String refreshToken)  {
	public static SigninResponse from(String accessToken, String refreshToken) {
		return new SigninResponse(accessToken, refreshToken);
	}
}
