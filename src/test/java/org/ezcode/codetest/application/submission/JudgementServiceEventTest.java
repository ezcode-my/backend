package org.ezcode.codetest.application.submission;

import static org.mockito.BDDMockito.*;

import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ProblemEventService;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.application.submission.service.JudgementService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.model.SubmissionResult;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("채점 서비스 이벤트 발행 테스트")
public class JudgementServiceEventTest {

    @InjectMocks
    private JudgementService judgementService;

    @Mock
    private SubmissionDomainService submissionDomainService;

    @Mock
    private SubmissionEventService submissionEventService;

    @Mock
    private ProblemEventService problemEventService;

    @Mock
    private SubmissionContext ctx;

    @Mock
    private SubmissionResult result;

    private String sessionKey;

    @BeforeEach
    void setUp() {
        sessionKey = "session-key";
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("테스트 케이스 목록 초기화 이벤트 발행")
        void publishInitTestcasesEvent() {

            // given
            given(ctx.getSessionKey()).willReturn(sessionKey);

            // when
            judgementService.publishInitTestcases(ctx);

            // then
            then(submissionEventService).should().publishInitTestcases(any(TestcaseListInitializedEvent.class));
        }

        @Test
        @DisplayName("채점 -> 최종 채점 결과 이벤트 발행")
        void publishFinalResultEvent() {

            // given
            given(submissionDomainService.finalizeSubmission(ctx)).willReturn(result);

            // when
            judgementService.finalizeAndPublish(ctx);

            // then
            then(submissionEventService).should().publishFinalResult(any(SubmissionJudgingFinishedEvent.class));
            then(problemEventService).should().publishProblemSolveEvent(result);
        }

        @Test
        @DisplayName("예외 발생 -> 에러 이벤트 발행")
        void publishErrorAndNotify() {

            // given
            SubmissionExceptionCode code = SubmissionExceptionCode.TESTCASE_TIMEOUT;
            SubmissionException se = new SubmissionException(code);

            // when
            judgementService.publishSubmissionError(sessionKey, se);

            // then
            then(submissionEventService).should().publishSubmissionError(
                argThat(ev ->
                    ev.sessionKey().equals(sessionKey) &&
                    ev.code().equals(code)
                )
            );
        }
    }
}
