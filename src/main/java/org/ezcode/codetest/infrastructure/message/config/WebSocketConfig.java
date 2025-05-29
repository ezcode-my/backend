package org.ezcode.codetest.infrastructure.message.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.setHandshakeHandler(new CustomHandShakeHandler())
			.withSockJS();
	}

	//현재 설정은 외부 브로커 안쓰고 내장 브로커 쓰는 방식.
	//추후 외부 브로커 구축완료되면 설정 변경 진행하겠습니다.

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/chat");
		registry.setUserDestinationPrefix("/user");

	}
}

