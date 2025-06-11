package org.ezcode.codetest.application.usermanagement.user.dto.request;


public record ModifyUserInfoRequest(
	String nickname,

	String githubUrl,

	String blogUrl,

	String profileImageUrl,

	String introduction,

	Integer age
) {
}
