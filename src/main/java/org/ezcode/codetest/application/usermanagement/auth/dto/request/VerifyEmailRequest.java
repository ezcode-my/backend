package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyEmailRequest {

	@NotBlank(message = "인증 번호는 공백일 수 없습니다")
	private int verificationCode;
}
