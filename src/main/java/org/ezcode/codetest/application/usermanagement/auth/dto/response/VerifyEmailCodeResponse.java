package org.ezcode.codetest.application.usermanagement.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증 번호 입력 응답")
public record VerifyEmailCodeResponse(
	@Schema(description = "인증 번호 성공 응답 메세지")
	String message,

	@Schema(description = "인증 성공 여부 true/false")
	boolean result
) {
	public static VerifyEmailCodeResponse from(String message, boolean result) {
		return new VerifyEmailCodeResponse(message, result);
	}
}
