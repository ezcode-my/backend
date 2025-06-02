package org.ezcode.codetest.application.usermanagement.user.dto;


public record ModifyUserInfoRequest(
	String nickname,

	String githubUrl,

	String blogUrl,

	String profileImageUrl,

	String introduction
) {
}
