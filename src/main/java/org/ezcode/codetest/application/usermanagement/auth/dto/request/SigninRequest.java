package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청")
public class SigninRequest {

	@Schema(description = "사용자 이메일 주소", example = "user@example.com")
	@NotBlank
	@Email(message = "이메일은 공백일 수 없습니다")
	private String email;

	@Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~20자)", example = "Aa1234**")
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	private String password;

}
