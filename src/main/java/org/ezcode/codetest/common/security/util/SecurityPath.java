package org.ezcode.codetest.common.security.util;

import org.springframework.stereotype.Component;

@Component
public class SecurityPath {
	public static final String[] PUBLIC_PATH = {
		"/api/auth/**",
		"/login",
		"/ezlogin",
		"/login/**",
		"/oauth2/**",
		"/login/oauth",
		"/login/oauth2/**", //OAuth로그인 접근
		"/actuator/**",
		"/chatting",
		"/submit-test",
		"/problems/**",
		"/ws/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v2/**",
		"/v3/**",
		"/webjars/**",
		"/searching",
		"/css/**", //html 화면 구성 접근
		"/images/**"
	};
}
