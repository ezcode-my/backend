package org.ezcode.codetest.infrastructure.security.config;

import java.util.List;

import org.ezcode.codetest.presentation.usermanagement.resolver.AuthUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthUserArgumentResolver authUserArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		log.info("----------------authUserArgumentResolver 등록---------------------");
		argumentResolvers.add(authUserArgumentResolver);
	}
}
