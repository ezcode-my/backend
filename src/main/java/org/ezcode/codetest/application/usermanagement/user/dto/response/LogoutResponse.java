package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그아웃 응답 DTO")
public class LogoutResponse {
	@Schema(description = "로그아웃 메시지", example = "성공적으로 로그아웃되었습니다.")
	String message;

	public LogoutResponse(String message) {
		this.message = message;
	}
}
