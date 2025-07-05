package org.ezcode.codetest.common.security.util;

import org.springframework.stereotype.Component;

@Component
public class SecurityPath {
	public static final String[] PUBLIC_PATH = {
		"/login/oauth2/**", //OAuth로그인 접근
		"/api/auth/**",
		"/api/oauth2/**",
		"/oauth/**",
		"/login",
		"/ezlogin",
		"/login/**",
		"/oauth2/**",
		"/login/oauth",
		"/actuator/**",
		"/chatting",
		"/submit-test/**",
		"/problems/**",
		"/ws/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/v2/**",
		"/v3/**",
		"/webjars/**",
		"/searching",
		"/css/**", //html 화면 구성 접근
		"/images/**",
		"/gaming",
		"/problems-test",
		"/test/**",
		"/html/**"
	};
}
