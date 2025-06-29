package org.ezcode.codetest.application.submission;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.application.submission.service.JudgementService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("채점 서비스 비동기 로직 테스트")
public class JudgementServiceAsyncTest {

    @InjectMocks
    private JudgementService judgementService;

    @Mock
    private SubmissionDomainService submissionDomainService;

    @Mock
    private SubmissionEventService submissionEventService;

    @Mock
    private JudgeClient judgeClient;

    @Mock
    private ExceptionNotifier exceptionNotifier;

    @Mock
    private SubmissionContext ctx;

    @Mock
    private JudgeResult judgeResult;

    @Mock
    private CountDownLatch latch;
    private int testcaseCount;
    private String sessionKey;
    private AtomicBoolean notified;
    private final Executor directExecutor = Runnable::run;

    @BeforeEach
    void setup() {
        testcaseCount = 5;
        sessionKey = "session-key";
        notified = new AtomicBoolean(false);
        ReflectionTestUtils.setField(judgementService, "judgeTestcaseExecutor", directExecutor);
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("runTestcases -> TestcaseEvaludatedEvent를 테스트케이스 개수만큼 발행")
        void runTestcasesPublishesEvaluations() throws InterruptedException {

            // given
            given(ctx.getTestcaseCount()).willReturn(testcaseCount);
            given(ctx.latch()).willReturn(latch);
            doReturn(true).when(latch).await(anyLong(), any(TimeUnit.class));
            given(judgeClient.submitAndGetToken(any())).willReturn("t1", "t2", "t3", "t4", "t5");
            given(judgeClient.pollUntilDone(anyString())).willReturn(judgeResult);

            given(submissionDomainService.handleEvaluationAndUpdateStats(any(), eq(ctx)))
                .willReturn(true)
                .willReturn(false)
                .willReturn(true)
                .willReturn(false)
                .willReturn(true);

            // when
            judgementService.runTestcases(ctx);

            // then
            then(submissionEventService).should(times(testcaseCount))
                .publishTestcaseUpdate(any(TestcaseEvaluatedEvent.class));
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("타임아웃 -> TESTCASE_TIMEOUT 예외 발생")
        void timeoutThrowsSubmissionException() throws InterruptedException {

            // given
            given(ctx.getTestcaseCount()).willReturn(testcaseCount);
            given(ctx.latch()).willReturn(latch);
            doReturn(false).when(latch).await(anyLong(), any(TimeUnit.class));

            // when & then
            assertThatThrownBy(() -> judgementService.runTestcases(ctx))
                .isInstanceOf(SubmissionException.class)
                .hasMessage(SubmissionExceptionCode.TESTCASE_TIMEOUT.getMessage());
        }

        @Test
        @DisplayName("judgeClient 예외 발생 -> 알림 호출 1회 & 에러 이벤트 발행")
        void runTestcaseAsyncExceptionNotifies() throws InterruptedException {

            // given
            given(ctx.getSessionKey()).willReturn(sessionKey);
            given(ctx.getTestcaseCount()).willReturn(testcaseCount);
            given(ctx.latch()).willReturn(latch);
            doReturn(true).when(latch).await(anyLong(), any(TimeUnit.class));

            given(ctx.notified()).willReturn(notified);
            doNothing().when(ctx).countDown();
            given(judgeClient.submitAndGetToken(any()))
                .willThrow(new SubmissionException(SubmissionExceptionCode.COMPILE_SERVER_ERROR));

            // when
            judgementService.runTestcases(ctx);

            // then
            then(submissionEventService).should(times(1)).publishSubmissionError(
                argThat(ev ->
                    ev.sessionKey().equals(ctx.getSessionKey()) &&
                    ev.code().equals(SubmissionExceptionCode.COMPILE_SERVER_ERROR)
                )
            );
            then(exceptionNotifier).should(times(1)).notifyException(
                eq("runTestcaseAsync"), any(SubmissionException.class)
            );
            then(ctx).should(times(5)).countDown();
        }
    }
}
