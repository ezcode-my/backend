package org.ezcode.codetest.common.security.util;

import org.springframework.stereotype.Component;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Component
public class PasswordEncoder {

	public String encode(String rawPassword) {
		return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
	}

	public boolean matches(String rawPassword, String encodedPassword) {
		BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
		return result.verified;
	}
}
