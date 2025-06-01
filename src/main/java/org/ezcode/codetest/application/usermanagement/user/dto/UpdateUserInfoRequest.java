package org.ezcode.codetest.application.usermanagement.user.dto;


public record UpdateUserInfoRequest(
	String nickname,

	String githubUrl,

	String blogUrl,

	String profileImageUrl,

	String introduction
) {
}
