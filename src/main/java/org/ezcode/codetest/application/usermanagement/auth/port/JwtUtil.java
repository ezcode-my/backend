package org.ezcode.codetest.application.usermanagement.auth.port;

import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;

import io.jsonwebtoken.Claims;

public interface JwtUtil {

	String createToken(Long userId, String email, UserRole userRole, String username, String nickname, Tier tier);

	String substringToken(String token);

	Claims extractClaims(String token);

	Long getExpiration(String token);

	Long getRemainingTime(String token);

	String createRefreshToken(Long userId);
}
