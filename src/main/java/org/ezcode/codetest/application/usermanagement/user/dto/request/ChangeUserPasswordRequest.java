package org.ezcode.codetest.application.usermanagement.user.dto.request;

public record ChangeUserPasswordRequest(
	String oldPassword,
	String newPassword
) {
}
