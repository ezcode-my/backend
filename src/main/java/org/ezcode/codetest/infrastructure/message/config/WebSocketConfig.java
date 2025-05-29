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
	/**
	 * 메시지 브로커를 구성하여 STOMP 메시지의 라우팅 경로와 브로커 동작을 설정합니다.
	 *
	 * 내장(Simple) 브로커를 "/topic" 및 "/queue" 경로에 활성화하여 브로드캐스트 및 큐 메시징을 지원하며,
	 * 애플리케이션 목적지 프리픽스를 "/chat"으로, 사용자별 메시지 프리픽스를 "/user"로 지정합니다.
	 */

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/chat");
		registry.setUserDestinationPrefix("/user");

	}
}

