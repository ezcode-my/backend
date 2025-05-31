package org.ezcode.codetest.application.usermanagement.auth.dto.signup;

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
public class SignupRequest {

	@NotBlank(message = "이메일은 공백일 수 없습니다")
	@Email
	@Size(max = 20, message = "이메일은 20자 이하로 입력해야 합니다")
	private String email;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	private String password;

	@NotBlank(message = "비밀번호 확인은 공백일 수 없습니다.")
	private String passwordConfirm;

	@NotBlank(message = "사용자명은 반드시 입력되어야합니다.")
	@Size(max = 15, message = "이름은 15글자 이하로 입력 가능합니다")
	private String username;

	@NotBlank(message = "별명은 반드시 입력되어야합니다")
	@Size(max = 20, message = "별명은 20글자 이하로 입력 가능합니다")
	private String nickname;

	//선택적 입력
	private Integer age;

}
