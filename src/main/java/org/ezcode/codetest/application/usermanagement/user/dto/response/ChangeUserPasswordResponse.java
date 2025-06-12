package org.ezcode.codetest.application.usermanagement.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 변경 응답 DTO")
public class ChangeUserPasswordResponse {
	@Schema(description = "응답 메시지", example = "비밀번호가 성공적으로 변경되었습니다.")
	String message;
	public ChangeUserPasswordResponse(String message) {
		this.message = message;
	}
}
