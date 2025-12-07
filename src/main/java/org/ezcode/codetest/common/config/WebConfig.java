package org.ezcode.codetest.common.config;

import org.ezcode.codetest.presentation.problemmanagement.problem.StringToDifficultyConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDifficultyConverter());
	}
}
