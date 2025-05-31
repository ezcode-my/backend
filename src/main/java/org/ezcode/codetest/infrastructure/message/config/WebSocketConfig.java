package org.ezcode.codetest.infrastructure.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${spring.message.activemq.address}")
	private String mqAddress;

	@Value("${spring.message.activemq.username}")
	private String mqUsername;

	@Value("${spring.message.activemq.password}")
	private String mqPassword;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry
			.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.setHandshakeHandler(new CustomHandShakeHandler())
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry
			.enableStompBrokerRelay("/topic", "/queue")
			.setRelayHost(mqAddress)
			.setRelayPort(61613)
			.setClientLogin(mqUsername)
			.setClientPasscode(mqPassword)
			.setSystemLogin(mqUsername)
			.setSystemPasscode(mqPassword);

		registry.setApplicationDestinationPrefixes("/chat");
		registry.setUserDestinationPrefix("/user");
	}
}

