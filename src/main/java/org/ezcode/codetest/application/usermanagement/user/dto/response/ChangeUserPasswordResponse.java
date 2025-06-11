package org.ezcode.codetest.application.usermanagement.user.dto.response;

import lombok.Getter;

@Getter
public class ChangeUserPasswordResponse {
	String message;
	public ChangeUserPasswordResponse(String message) {
		this.message = message;
	}
}
