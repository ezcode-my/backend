package org.ezcode.codetest.infrastructure.event;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Map;

import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.ezcode.codetest.infrastructure.event.listener.RedisJudgeQueueConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisJudgeQueueConsumer 단위 테스트")
public class RedisJudgeQueueConsumerTest {

    @InjectMocks
    private RedisJudgeQueueConsumer consumer;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private StreamOperations<String, Object, Object> streamOps;

    @Mock
    private MapRecord<String, String, String> record;

    @BeforeEach
    void setUp() {
        Map<String, String> payload = Map.of(
            "sessionKey",   "session-key",
            "problemId",    "42",
            "languageId",   "7",
            "userId",       "100",
            "sourceCode",   "System.out.println(123);"
        );
        RecordId recordId = RecordId.of("1-0");

        given(record.getValue()).willReturn(payload);
        given(record.getId()).willReturn(recordId);
    }

    @Test
    @DisplayName("정상 처리 -> processSubmissionAsync & ack 호출")
    void onMessage_success() {

        // given
        given(redisTemplate.opsForStream()).willReturn(streamOps);

        // when
        consumer.onMessage(record);

        // then
        ArgumentCaptor<SubmissionMessage> captor = ArgumentCaptor.forClass(SubmissionMessage.class);
        then(submissionService).should().processSubmissionAsync(captor.capture());

        SubmissionMessage sent = captor.getValue();

        assertAll(
            () -> assertThat(sent.sessionKey()).isEqualTo("session-key"),
            () -> assertThat(sent.problemId()).isEqualTo(42L),
            () -> assertThat(sent.languageId()).isEqualTo(7L),
            () -> assertThat(sent.userId()).isEqualTo(100L),
            () -> assertThat(sent.sourceCode()).isEqualTo("System.out.println(123);")
        );

        then(redisTemplate.opsForStream()).should().acknowledge("judge-group", record);
    }

    @Test
    @DisplayName("processSubmissionAsync 에러 -> SubmissionException & ack 미호출")
    void onMessage_failure() {

        // given
        doThrow(new RuntimeException("oops"))
            .when(submissionService).processSubmissionAsync(any());

        // when & then
        assertThatThrownBy(() -> consumer.onMessage(record))
            .isInstanceOf(SubmissionException.class)
            .hasMessage(SubmissionExceptionCode.REDIS_SERVER_ERROR.getMessage());

        then(streamOps).should(never()).acknowledge(anyString(), any());
    }
}
