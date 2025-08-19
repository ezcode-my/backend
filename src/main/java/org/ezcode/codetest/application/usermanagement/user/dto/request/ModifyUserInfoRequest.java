package org.ezcode.codetest.application.usermanagement.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 변경 요청")
public record ModifyUserInfoRequest(
	@Schema(description = "변경할 별명", example = "코딩짱")
	String nickname,

	@Schema(description = "GitHub 주소", example = "https://github.com/username")
	String githubUrl,

	@Schema(description = "블로그 주소", example = "https://blog.example.com")
	String blogUrl,

	@Schema(description = "자기소개", example = "안녕하세요, 백엔드 개발자입니다.")
	String introduction,

	@Schema(description = "나이", example = "28")
	Integer age
) {
}
