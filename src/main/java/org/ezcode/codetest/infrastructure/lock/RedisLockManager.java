package org.ezcode.codetest.infrastructure.lock;

import java.time.Duration;

import org.ezcode.codetest.application.submission.port.LockManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockManager implements LockManager {

	private final StringRedisTemplate redisTemplate;

	private static final String LOCK_KEY_FORMAT = "%s-lock:user:%d:problem:%d";
	private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

	@Override
	public boolean tryLock(String prefix, Long userId, Long problemId) {
		String key = getKey(prefix, userId, problemId);
		Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "LOCKED", LOCK_DURATION);
		return Boolean.TRUE.equals(success);
	}

	@Override
	public void releaseLock(String prefix, Long userId, Long problemId) {
		redisTemplate.delete(getKey(prefix, userId, problemId));
	}

	private String getKey(String prefix, Long userId, Long problemId) {
		return LOCK_KEY_FORMAT.formatted(prefix, userId, problemId);
	}
}
