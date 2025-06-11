package org.ezcode.codetest.application.usermanagement.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeUserPasswordRequest(
	String oldPassword,

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	String newPassword
) {
}
