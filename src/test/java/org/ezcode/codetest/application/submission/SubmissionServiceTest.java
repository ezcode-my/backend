package org.ezcode.codetest.application.submission;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.LockManager;
import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.application.submission.service.GitHubPushService;
import org.ezcode.codetest.application.submission.service.JudgementService;
import org.ezcode.codetest.application.submission.service.SubmissionService;
import org.ezcode.codetest.domain.language.exception.LanguageException;
import org.ezcode.codetest.domain.language.exception.code.LanguageExceptionCode;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("제출 서비스 테스트")
public class SubmissionServiceTest {

    @InjectMocks
    private SubmissionService submissionService;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private ProblemDomainService problemDomainService;

    @Mock
    private LanguageDomainService languageDomainService;

    @Mock
    private SubmissionDomainService submissionDomainService;

    @Mock
    private QueueProducer queueProducer;

    @Mock
    private ExceptionNotifier exceptionNotifier;

    @Mock
    private LockManager lockManager;

    @Mock
    private JudgementService judgementService;

    @Mock
    private GitHubPushService gitHubPushService;

    @Mock
    private AuthUser authUser;

    @Mock
    private User user;

    @Mock
    private Language language;

    @Mock
    private ProblemInfo info;

    @Mock
    private CodeSubmitRequest request;

    @Mock
    private SubmissionMessage msg;

    private Long userId;
    private Long problemId;
    private Long languageId;
    private String sourceCode;
    private String sessionKey;
    private ReviewResult reviewResult;

    @BeforeEach
    void setup() {
        userId = 1L;
        problemId = 100L;
        languageId = 2L;
        sourceCode = "TEST";
        sessionKey = userId + "_";
        reviewResult = new ReviewResult("good");
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("락 획득 성공 -> 메세지 큐에 전송하고 sessionKey 반환")
        void enqueueCodeSubmissionSucceeds() {

            // given
            given(authUser.getId()).willReturn(userId);
            given(request.languageId()).willReturn(languageId);
            given(request.sourceCode()).willReturn(sourceCode);
            given(lockManager.tryLock("submission", authUser.getId(), problemId))
                .willReturn(true);

            // when
            String result = submissionService.enqueueCodeSubmission(problemId, request, authUser);

            then(queueProducer).should()
                .enqueue(argThat(msg ->
                    msg.sessionKey().startsWith(sessionKey) &&
                        msg.problemId().equals(problemId) &&
                        msg.languageId().equals(languageId) &&
                        msg.sourceCode().equals(sourceCode)
                ));
            assertThat(result).startsWith(sessionKey);
        }

        @Test
        @DisplayName("정상 흐름 -> 모든 서비스 호출 및 락 해제")
        void runsThroughAllStepsAndReleasesLock() throws InterruptedException {

            // given
            given(msg.userId()).willReturn(userId);
            given(msg.languageId()).willReturn(languageId);
            given(msg.problemId()).willReturn(problemId);
            given(userDomainService.getUserById(userId)).willReturn(user);
            given(languageDomainService.getLanguage(languageId)).willReturn(language);
            given(problemDomainService.getProblemInfo(problemId)).willReturn(info);

            // when
            submissionService.processSubmissionAsync(msg);

            // then
            ArgumentCaptor<SubmissionContext> captor = ArgumentCaptor.forClass(SubmissionContext.class);
            then(judgementService).should().publishInitTestcases(captor.capture());
            SubmissionContext ctx = captor.getValue();

            then(judgementService).should().runTestcases(ctx);
            then(judgementService).should().finalizeAndPublish(ctx);
            then(gitHubPushService).should().pushSolutionToRepo(ctx);

            then(lockManager).should().releaseLock("submission", userId, problemId);
        }

        @Test
        @DisplayName("제출 내역을 문제 기준으로 그룹핑해서 반환")
        void returnsGroupedSubmissions() {

            // given
            given(authUser.getId()).willReturn(userId);
            given(userDomainService.getUserById(userId)).willReturn(user);
            given(user.getId()).willReturn(userId);

            Submission s1 = mock(Submission.class);
            Submission s2 = mock(Submission.class);
            Submission s3 = mock(Submission.class);

            LocalDateTime now = LocalDateTime.now().withNano(0);
            given(s1.getCreatedAt()).willReturn(now);
            given(s2.getCreatedAt()).willReturn(now);
            given(s3.getCreatedAt()).willReturn(now);

            Problem p42 = mock(Problem.class);
            given(p42.getId()).willReturn(42L);
            given(s1.getProblem()).willReturn(p42);
            given(s2.getProblem()).willReturn(p42);

            Problem p99 = mock(Problem.class);
            given(p99.getId()).willReturn(99L);
            given(s3.getProblem()).willReturn(p99);

            given(submissionDomainService.getSubmissions(userId)).willReturn(List.of(s1, s2, s3));

            // when
            List<GroupedSubmissionResponse> groups = submissionService.getSubmissions(authUser);

            // then
            then(submissionDomainService).should().getSubmissions(userId);
            assertThat(groups).hasSize(2);
            GroupedSubmissionResponse group42 = groups.stream()
                .filter(g -> g.getProblemId() == 42L)
                .findFirst().orElseThrow();
            assertThat(group42.getSubmissions()).hasSize(2);

            GroupedSubmissionResponse group99 = groups.stream()
                .filter(g -> g.getProblemId() == 99L)
                .findFirst().orElseThrow();
            assertThat(group99.getSubmissions()).hasSize(1);
        }

        @Test
        @DisplayName("정상 호출 -> 토큰 차감, 리뷰 요청 & 응답 반환")
        void returnsReviewResponse() {

            // given
            given(authUser.getId()).willReturn(userId);
            given(userDomainService.getUserById(authUser.getId())).willReturn(user);
            willDoNothing().given(userDomainService).decreaseReviewToken(user);

            Problem problem = mock(Problem.class);
            given(problemDomainService.getProblem(problemId)).willReturn(problem);

            CodeReviewRequest req = mock(CodeReviewRequest.class);
            given(req.languageId()).willReturn(languageId);
            given(languageDomainService.getLanguage(languageId)).willReturn(language);
            given(reviewClient.requestReview(any())).willReturn(reviewResult);

            // when
            CodeReviewResponse resp = submissionService.getCodeReview(problemId, req, authUser);

            // then
            then(userDomainService).should().getUserById(userId);
            then(userDomainService).should().decreaseReviewToken(user);
            assertThat(resp.reviewContent()).isEqualTo(reviewResult.reviewContent());
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("락 획득 실패 -> ALREADY_JUDGING 예외")
        void enqueueCodeSubmissionFailed() {

            // given
            given(authUser.getId()).willReturn(userId);
            given(lockManager.tryLock("submission", authUser.getId(), problemId))
                .willReturn(false);

            // when & then
            assertThatThrownBy(() ->
                submissionService.enqueueCodeSubmission(problemId, request, authUser)
            )
                .isInstanceOf(SubmissionException.class)
                .hasMessage(SubmissionExceptionCode.ALREADY_JUDGING.getMessage());
        }

        @Test
        @DisplayName("예외 -> 에러 이벤트 발행 & 락 해제")
        void processFailureNotifiesAndReleasesLock() {

            // given
            given(msg.userId()).willReturn(userId);
            given(msg.languageId()).willReturn(languageId);
            given(msg.problemId()).willReturn(problemId);
            given(msg.sessionKey()).willReturn(sessionKey);
            given(userDomainService.getUserById(userId)).willReturn(user);
            given(languageDomainService.getLanguage(languageId))
                .willThrow(new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND));

            // when
            submissionService.processSubmissionAsync(msg);

            // then
            then(judgementService).should().publishSubmissionError(
                eq(sessionKey),
                any(LanguageException.class)
            );
            then(exceptionNotifier).should().notifyException(
                eq("submitCodeStream"), any(LanguageException.class)
            );
            then(lockManager).should().releaseLock("submission", userId, problemId);
        }
    }
}
