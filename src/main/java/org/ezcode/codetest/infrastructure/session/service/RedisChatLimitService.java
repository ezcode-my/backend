package org.ezcode.codetest.infrastructure.session.service;

import java.time.Duration;
import java.util.Collections;

import org.ezcode.codetest.application.chatting.port.session.ChatLimitService;
import org.ezcode.codetest.infrastructure.session.constant.RedisKeyConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisChatLimitService implements ChatLimitService {

	private final RedisTemplate<String, Long> redisTemplate;

	private final RedisScript<Long> incrWithTtlScript =
		new DefaultRedisScript<>(
			"local current = redis.call('INCR', KEYS[1])\n" +
				"if tonumber(current) == 1 then\n" +
				"  redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1]))\n" +
				"end\n" +
				"return tonumber(current)",
			Long.class
		);

	@Override
	public Long increaseChatCount(String email) {

		String sessionKey = RedisKeyConstants.CHAT_COUNT_KEY_PREFIX + email;

		return redisTemplate.execute(
			incrWithTtlScript,
			Collections.singletonList(sessionKey),
			String.valueOf(RedisKeyConstants.CHAT_COUNT_TTL)
		);
	}

	public void applyChatBlock(String email) {

		String sessionKey = RedisKeyConstants.BLOCKED_SESSION_KEY_PREFIX + email;

		redisTemplate.opsForValue().set(
			sessionKey,
			1L,
			Duration.ofSeconds(RedisKeyConstants.BLOCKED_SESSION_TTL)
		);
	}

	public Boolean isBlocked(String email) {

		String sessionKey = RedisKeyConstants.BLOCKED_SESSION_KEY_PREFIX + email;

		Long isBlocked = redisTemplate.opsForValue().get(sessionKey);

		return isBlocked != null;
	}
}
