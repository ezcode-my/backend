package org.ezcode.codetest.infrastructure.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisSessionConfig {

	@Bean
	public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Long> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);

		GenericToStringSerializer<Long> longSerializer = new GenericToStringSerializer<>(Long.class);
		template.setValueSerializer(longSerializer);
		template.setHashValueSerializer(longSerializer);

		return template;
	}
}
