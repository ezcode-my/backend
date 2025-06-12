package org.ezcode.codetest.application.usermanagement.auth.dto.request;

import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.AuthExceptionCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutRequest {
	@NotBlank(message = "토큰이 필요합니다")
	private HttpServletRequest request;

	public String getHeader() {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new AuthException(AuthExceptionCode.INVALID_AUTHORIZATION_HEADER);
		}
		return bearerToken.substring(7);
	}

}
