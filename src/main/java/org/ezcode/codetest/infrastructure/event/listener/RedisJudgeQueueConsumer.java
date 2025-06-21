package org.ezcode.codetest.infrastructure.event.listener;

import java.util.Map;

import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.infrastructure.event.dto.SubmissionMessage;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisJudgeQueueConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final SubmissionService submissionService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map<String, String> values = message.getValue();

        SubmissionMessage msg = new SubmissionMessage(
            values.get("emitterKey"),
            Long.valueOf(values.get("problemId")),
            Long.valueOf(values.get("languageId")),
            Long.valueOf(values.get("userId")),
            values.get("sourceCode")
        );

        try {
            log.info("[컨슈머 수신] {}", msg.emitterKey());
            submissionService.submitCodeStream(msg);

            log.info("[컨슈머 ACK] messageId={}", message.getId());
            redisTemplate.opsForStream().acknowledge("judge-group", message);
        } catch (Exception e) {
            log.error("채점 메시지 처리 실패: {}", message.getId(), e);
            throw new SubmissionException(SubmissionExceptionCode.REDIS_SERVER_ERROR);
        }
    }
}
