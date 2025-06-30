package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "회원가입 요청")
public class SignupRequest {

	@Schema(description = "유저 이메일", example = "email@gmail.com")
	@NotBlank(message = "이메일은 공백일 수 없습니다")
	@Email
	private String email;

	@Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함 8~20자)", example = "Aa12345**")
	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
	private String password;


	@Schema(description = "비밀번호 확인", example = "Aa12345**")
	@NotBlank(message = "비밀번호 확인은 공백일 수 없습니다.")
	private String passwordConfirm;


	@Schema(description = "사용자 이름 (최대 15자)", example = "홍길동")
	@NotBlank(message = "사용자명은 반드시 입력되어야합니다.")
	@Size(max = 15, message = "이름은 15글자 이하로 입력 가능합니다")
	private String username;

	//선택적 입력
	@Schema(description = "나이 (선택 입력)", example = "25")
	private Integer age;

}
