package org.ezcode.codetest.infrastructure.event.config;

import java.time.Duration;

import org.ezcode.codetest.infrastructure.event.listener.RedisJudgeQueueConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.connection.stream.Consumer;

@Configuration
public class RedisStreamConfig {

	@Bean
	public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
		RedisConnectionFactory factory,
		RedisJudgeQueueConsumer consumer
	) {
		StreamMessageListenerContainer
			.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
			StreamMessageListenerContainer
				.StreamMessageListenerContainerOptions
				.builder()
				.pollTimeout(Duration.ofSeconds(1))
				.build();

		StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
			StreamMessageListenerContainer.create(factory, options);

		container.receive(
			Consumer.from("judge-group", "consumer-1"),
			StreamOffset.create("judge-queue", ReadOffset.lastConsumed()),
			consumer
		);

		container.start();
		return container;
	}
}
