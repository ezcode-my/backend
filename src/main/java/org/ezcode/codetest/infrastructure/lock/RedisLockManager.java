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

	private static final String LOCK_KEY_FORMAT = "submission-lock:user:%d:problem:%d";
	private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

	@Override
	public boolean tryLock(Long userId, Long problemId) {
		String key = getKey(userId, problemId);
		Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "LOCKED", LOCK_DURATION);
		return Boolean.TRUE.equals(success);
	}

	@Override
	public void releaseLock(Long userId, Long problemId) {
		redisTemplate.delete(getKey(userId, problemId));
	}

	private String getKey(Long userId, Long problemId) {
		return LOCK_KEY_FORMAT.formatted(userId, problemId);
	}
}
