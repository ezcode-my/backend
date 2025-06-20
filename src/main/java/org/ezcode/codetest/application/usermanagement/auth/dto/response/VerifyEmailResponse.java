package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증 번호 입력 응답")
public record VerifyEmailResponse(
	@Schema(description = "생성된 accesstoken")
	String accessToken,

	@Schema(description = "생성된 refreshToken")
	String refreshToken
) {
	public static VerifyEmailResponse from(String accessToken, String refreshToken) {
		return new VerifyEmailResponse(accessToken, refreshToken);
	}
}
