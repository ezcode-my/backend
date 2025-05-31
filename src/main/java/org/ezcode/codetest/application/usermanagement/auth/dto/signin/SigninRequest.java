package org.ezcode.codetest.application.usermanagement.auth.dto.signin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

	@NotBlank
	@Email(message = "이메일은 공백일 수 없습니다")
	@Size(max = 20, message = "이메일은 20자 이하로 입력해야 합니다")
	private String email;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	private String password;

}
