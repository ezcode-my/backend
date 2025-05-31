package org.ezcode.codetest.common.config;

import org.ezcode.codetest.infrastructure.security.jwt.JwtFilter;
import org.ezcode.codetest.infrastructure.security.jwt.JwtUtilImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

	private final JwtUtilImpl jwtUtilImpl;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter(){
		FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new JwtFilter(jwtUtilImpl));
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;

	}
}
