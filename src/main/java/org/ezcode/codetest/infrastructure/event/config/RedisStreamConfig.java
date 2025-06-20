package org.ezcode.codetest.infrastructure.event.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

import org.ezcode.codetest.infrastructure.event.listener.RedisJudgeQueueConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.connection.stream.Consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {

    private final RedisTemplate<String, String> redisTemplate;
    private final Executor consumerExecutor;

    @PostConstruct
    public void initConsumerGroup() {
        try {
            Boolean exists = redisTemplate.hasKey("judge-queue");

            if (Boolean.FALSE.equals(exists)) {
                log.info("Redis Stream 'judge-queue'를 생성합니다.");
                redisTemplate.opsForStream().add("judge-queue", Map.of(
                    "emitterKey", "dummy",
                    "problemId", "0",
                    "languageId", "0",
                    "userId", "0",
                    "sourceCode", "dummy"
                ));
            }

            try {
                log.info("Redis Consumer Group의 기존 컨슈머 삭제 시도");
                redisTemplate.execute((RedisConnection connection) -> {
                    connection.xGroupDelConsumer(
                        "judge-queue".getBytes(),
                        "judge-group",
                        "consumer-1"
                    );
                    return null;
                });
            } catch (Exception e) {
                log.warn("DELCONSUMER 중 오류 발생: {}", e.getMessage());
            }

            redisTemplate.opsForStream().createGroup("judge-queue", ReadOffset.latest(), "judge-group");

            log.info("Redis Stream 'judge-queue'에 대한 Consumer Group 'judge-group'을 생성했습니다.");
        } catch (Exception e) {
            log.error("예외 발생: {}, 메시지: {}", e.getClass(), e.getMessage());
            if (e.getCause() instanceof io.lettuce.core.RedisBusyException) {
                log.info("Redis Consumer Group 'judge-group'이 이미 존재하여 생성을 건너뜁니다.");
            } else {
                log.error("Redis Consumer Group 초기화에 실패했습니다.", e);
                throw e;
            }
        }
    }

    @Bean(destroyMethod = "stop")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
        RedisConnectionFactory factory,
        RedisJudgeQueueConsumer consumer
    ) {
        StreamMessageListenerContainer
            .StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
            StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .executor(consumerExecutor)
                .pollTimeout(Duration.ofSeconds(2))
                .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
            StreamMessageListenerContainer.create(factory, options);

        container.receive(
            Consumer.from("judge-group", "consumer-1"),
            StreamOffset.create("judge-queue", ReadOffset.lastConsumed()),
            consumer
        );

        container.start();
        return container;
    }
}
