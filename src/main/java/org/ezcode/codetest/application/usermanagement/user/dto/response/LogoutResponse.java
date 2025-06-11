package org.ezcode.codetest.application.usermanagement.user.dto.response;

import lombok.Getter;

@Getter
public class LogoutResponse {
	String message;

	public LogoutResponse(String message) {
		this.message = message;
	}
}
