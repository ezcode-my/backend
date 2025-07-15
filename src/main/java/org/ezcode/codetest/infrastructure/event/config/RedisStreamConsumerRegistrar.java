package org.ezcode.codetest.infrastructure.event.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.ezcode.codetest.infrastructure.event.listener.RedisJudgeQueueConsumer;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class RedisStreamConsumerRegistrar {

    private final String consumerName;

    public RedisStreamConsumerRegistrar() {
        this.consumerName = initConsumerName();
    }

    public void registerConsumer(StreamMessageListenerContainer<String, MapRecord<String, String, String>> container,
        RedisJudgeQueueConsumer consumer) {
        container.receive(
            Consumer.from("judge-group", consumerName),
            StreamOffset.create("judge-queue", ReadOffset.lastConsumed()),
            consumer
        );
    }

    private String initConsumerName() {
        try {
            return "consumer-" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("호스트명 확인 실패, UUID 사용: {}", e.getMessage());
            return "consumer-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }
}
