package org.ezcode.codetest.common.config;

import org.ezcode.codetest.infrastructure.security.jwt.JwtFilter;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

	private final JwtUtil jwtUtil;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter(){
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new JwtFilter(jwtUtil));
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;

	}
}
