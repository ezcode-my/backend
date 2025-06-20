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
                .trim("judge-queue", 5);

            log.info("Redis Stream 트림(정리) 작업이 실행되었습니다. 삭제된 메시지 개수: {}", trimmed);
        } catch (Exception e) {
            log.error("Redis Stream 트림(정리) 작업에 실패했습니다.", e);
        }
    }
}
