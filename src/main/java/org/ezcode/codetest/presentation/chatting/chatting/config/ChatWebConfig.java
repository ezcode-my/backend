package org.ezcode.codetest.presentation.chatting.chatting.config;

import org.ezcode.codetest.presentation.chatting.chatting.interceptor.ChatLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ChatWebConfig implements WebMvcConfigurer {

	private final ChatLimitInterceptor chatSpamInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(chatSpamInterceptor)
			.addPathPatterns("/api/rooms/*/chat");
	}
}
