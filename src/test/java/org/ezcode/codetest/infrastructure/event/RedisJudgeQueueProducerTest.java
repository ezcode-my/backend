package org.ezcode.codetest.infrastructure.event;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Map;

import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.ezcode.codetest.infrastructure.event.publisher.RedisJudgeQueueProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisJudgeQueueProducer 단위 테스트")
public class RedisJudgeQueueProducerTest {

    @InjectMocks
    private RedisJudgeQueueProducer producer;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private StreamOperations<String, Object, Object> streamOps;

    @BeforeEach
    void setUp() {
        given(redisTemplate.opsForStream()).willReturn(streamOps);
    }

    @Test
    @DisplayName("enqueue -> judge-queue에 메시지 추가")
    void enqueue_addToStream() {

        // given
        SubmissionMessage msg = new SubmissionMessage(
            "session-key",
            42L,
            7L,
            100L,
            "System.out.println(123);"
        );

        // when
        producer.enqueue(msg);

        // then
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);

        then(streamOps).should()
            .add(eq("judge-queue"), captor.capture());

        Map<String, String> actual = captor.getValue();
        assertAll(
            () -> assertThat(actual.get("sessionKey")).isEqualTo("session-key"),
            () -> assertThat(actual.get("problemId")).isEqualTo("42"),
            () -> assertThat(actual.get("languageId")).isEqualTo("7"),
            () -> assertThat(actual.get("userId")).isEqualTo("100"),
            () -> assertThat(actual.get("sourceCode")).isEqualTo("System.out.println(123);")
        );
    }
}
