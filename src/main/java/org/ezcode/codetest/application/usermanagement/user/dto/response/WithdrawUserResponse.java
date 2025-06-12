package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 탈퇴 응답 DTO")
public record WithdrawUserResponse(
	@Schema(description = "응답 메시지", example = "회원 탈퇴가 완료되었습니다.")
	String message
) {}
