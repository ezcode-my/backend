package org.ezcode.codetest.infrastructure.security.config;

import org.ezcode.codetest.infrastructure.security.jwt.JwtFilter;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtilImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
	private final JwtUtilImpl jwtUtil;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter(jwtUtil));
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}
}
