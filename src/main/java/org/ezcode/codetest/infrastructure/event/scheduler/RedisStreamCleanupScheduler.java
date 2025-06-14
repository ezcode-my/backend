package org.ezcode.codetest.infrastructure.event.scheduler;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamCleanupScheduler {

	private final StringRedisTemplate redisTemplate;

	@Scheduled(fixedRate = 600000)
	public void cleanUpOldMessages() {
		try {
			Long trimmed = redisTemplate.opsForStream()
				.trim("judge-queue", 100);

			log.info("Redis Stream trim executed, deleted count: {}", trimmed);
		} catch (Exception e) {
			log.error("Redis Stream trim failed", e);
		}
	}
}
