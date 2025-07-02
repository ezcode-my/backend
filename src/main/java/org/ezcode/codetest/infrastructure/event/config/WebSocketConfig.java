package org.ezcode.codetest.infrastructure.event.config;

import org.ezcode.codetest.common.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtUtil jwtUtil;

	@Value("${spring.message.activemq.address}")
	private String mqAddress;

	@Value("${spring.message.activemq.username}")
	private String mqUsername;

	@Value("${spring.message.activemq.password}")
	private String mqPassword;

	@Value("${spring.message.activemq.port}")
	private Integer mqPort;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry
			.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.setHandshakeHandler(new CustomHandShakeHandler(jwtUtil))
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry
			.enableStompBrokerRelay("/topic", "/queue")
			.setRelayHost(mqAddress)
			.setRelayPort(mqPort)
			.setClientLogin(mqUsername)
			.setClientPasscode(mqPassword)
			.setSystemLogin(mqUsername)
			.setSystemPasscode(mqPassword)
			.setUserDestinationBroadcast("/topic/simp-user-registry")
			.setUserRegistryBroadcast("/topic/simp-user-registry");

		registry.setApplicationDestinationPrefixes("/chat", "/app");
		registry.setUserDestinationPrefix("/user");
	}

}

