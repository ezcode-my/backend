package org.ezcode.codetest.application.usermanagement.auth.port;

import org.ezcode.codetest.domain.user.model.enums.UserRole;

import io.jsonwebtoken.Claims;

public interface JwtUtil {

	String createToken(Long userId, String email, UserRole userRole, String username, String nickname);

	String substringToken(String token);

	Claims extractClaims(String token);
}
