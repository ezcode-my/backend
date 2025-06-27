package org.ezcode.codetest.infrastructure.event.publisher;

import java.util.Map;

import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisJudgeQueueProducer implements QueueProducer {

    private final RedisTemplate<String, String> redisTemplate;

    public void enqueue(SubmissionMessage submissionMessage) {
        log.info("[WS enqueue] sessionKey: {}", submissionMessage.sessionKey());
        Map<String, String> map = Map.of(
            "sessionKey", submissionMessage.sessionKey(),
            "problemId", submissionMessage.problemId().toString(),
            "languageId", submissionMessage.languageId().toString(),
            "userId", submissionMessage.userId().toString(),
            "sourceCode", submissionMessage.sourceCode()
        );

        redisTemplate.opsForStream().add("judge-queue", map);
    }
}
