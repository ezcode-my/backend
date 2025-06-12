package org.ezcode.codetest.application.usermanagement.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "비밀번호 변경 요청")
public record ChangeUserPasswordRequest(

	@Schema(description = "기존 비밀번호와 일치해야합니다", example = "Aa1234**")
	@NotBlank
	String oldPassword,

	@Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~20자)", example = "Aa1234**")
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	String newPassword
) {
}
