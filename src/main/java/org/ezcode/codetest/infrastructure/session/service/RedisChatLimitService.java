package org.ezcode.codetest.infrastructure.session.service;

import java.time.Duration;

import org.ezcode.codetest.application.chatting.port.session.ChattingLimitService;
import org.ezcode.codetest.infrastructure.session.constant.RedisKeyConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisChatLimitService implements ChattingLimitService {

	private final RedisTemplate<String, Long> redisTemplate;

	public Long increaseChatCount(String email) {

		String sessionKey = RedisKeyConstants.CHAT_COUNT_KEY_PREFIX + email;

		Long count = redisTemplate.opsForValue().increment(sessionKey);

		if (count != null && count == 1L) {
			redisTemplate.expire(
				sessionKey,
				Duration.ofSeconds(RedisKeyConstants.CHAT_COUNT_TTL)
			);
		}

		return count;
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
