package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "refreshToken 응답")
public record RefreshTokenResponse (
	@Schema(description = "생성된 refreshToken")
	String token
) {
	public static RefreshTokenResponse from(String token) {
		return new RefreshTokenResponse(token);
	}
}
