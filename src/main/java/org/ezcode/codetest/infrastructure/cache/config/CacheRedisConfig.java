package org.ezcode.codetest.infrastructure.cache.config;

import java.util.List;

import org.ezcode.codetest.domain.chat.model.ChatRoom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CacheRedisConfig {

	@Bean
	public RedisTemplate<String, List<ChatRoom>> cacheRedisTemplate(
		RedisConnectionFactory factory,
		ObjectMapper objectMapper
	) {
		RedisTemplate<String, List<ChatRoom>> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		GenericToStringSerializer<String> keySerializer =
			new GenericToStringSerializer<>(String.class);

		template.setKeySerializer(keySerializer);

		JavaType eventType = objectMapper.getTypeFactory()
			.constructCollectionType(List.class, ChatRoom.class);

		Jackson2JsonRedisSerializer<List<ChatRoom>> valueSerializer =
			new Jackson2JsonRedisSerializer<>(objectMapper, eventType);

		template.setValueSerializer(valueSerializer);

		return template;
	}
}
