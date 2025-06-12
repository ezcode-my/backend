package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "소셜 로그인 응답")
public record OAuthResponse(
	@Schema(description = "accessToken")
	String accessToken,
	@Schema(description = "refreshToken")
	String refreshToken
){
	public static OAuthResponse from(String accessToken, String refreshToken) {
		return new OAuthResponse(accessToken, refreshToken);
	}
}
