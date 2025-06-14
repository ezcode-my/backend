package org.ezcode.codetest.infrastructure.cache.config;

import org.ezcode.codetest.application.chatting.port.cache.ChatRoomCache;
import org.ezcode.codetest.infrastructure.event.dto.NotificationRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisCacheConfig {

	@Bean
	public RedisTemplate<String, ChatRoomCache> cacheRedisTemplate(
		RedisConnectionFactory factory,
		ObjectMapper objectMapper
	) {
		RedisTemplate<String, ChatRoomCache> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		GenericToStringSerializer<String> keySerializer =
			new GenericToStringSerializer<>(String.class);

		template.setKeySerializer(keySerializer);

		JavaType eventType = objectMapper.getTypeFactory()
			.constructType(ChatRoomCache.class);

		Jackson2JsonRedisSerializer<ChatRoomCache> valueSerializer =
			new Jackson2JsonRedisSerializer<>(objectMapper, eventType);

		template.setValueSerializer(valueSerializer);

		return template;
	}

	@Bean
	public RedisTemplate<String, NotificationRecord> notificationRedisTemplate(
		RedisConnectionFactory factory,
		ObjectMapper objectMapper
	) {

		RedisTemplate<String, NotificationRecord> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		template.setKeySerializer(new StringRedisSerializer());

		Jackson2JsonRedisSerializer<NotificationRecord> valueSerializer =
			new Jackson2JsonRedisSerializer<>(objectMapper, NotificationRecord.class);
		template.setValueSerializer(valueSerializer);

		return template;
	}
}
