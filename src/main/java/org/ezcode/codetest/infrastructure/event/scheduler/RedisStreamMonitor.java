package org.ezcode.codetest.infrastructure.event.scheduler;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStreamMonitor {

    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;

    // Redis Stream 컨테이너가 죽었는지 감시하고, 죽어 있으면 재시작 + 컨슈머 재등록
    @Scheduled(fixedDelay = 300_000)
    public void monitor() {
        if (!container.isRunning()) {
            log.warn("[Redis Listener] 죽어 있음 -> 컨테이너 재시작 시도");

            try {
                container.start();
                log.info("[Redis Listener] 컨테이너 재시작 완료");
            } catch (Exception e) {
                log.error("[Redis Listener] 재시작 실패", e);
            }
        }
    }
}
