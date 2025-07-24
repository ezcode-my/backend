package org.ezcode.codetest.application.submission;

import static org.mockito.BDDMockito.*;

import org.ezcode.codetest.application.submission.dto.event.GitPushStatusEvent;
import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.application.submission.service.GitHubPushService;
import org.ezcode.codetest.common.security.util.AESUtil;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.service.UserGithubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("깃허브 푸시 서비스 테스트")
public class GitHubPushServiceTest {

    @InjectMocks
    private GitHubPushService gitHubPushService;

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private UserGithubService userGithubService;

    @Mock
    private ExceptionNotifier exceptionNotifier;

    @Mock
    private SubmissionEventService eventService;

    @Mock
    private AESUtil aesUtil;

    @Mock
    private UserGithubInfo info;

    @Mock
    private SubmissionContext ctx;

    @Mock
    private GitHubPushRequest req;

    private Long userId;
    private String sessionKey;
    private String encryptedToken;
    private String decryptedToken;

    @BeforeEach
    void setUp() {
        userId = 1L;
        sessionKey = "session-key";
        encryptedToken = "encrypted-token";
        decryptedToken = "decrypted-token";
    }

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @Test
        @DisplayName("GitPushStatus 비활성 -> 아무 동작도 하지 않음")
        void noPushWhenDisabled() {

            // given
            given(ctx.isGitPushStatus()).willReturn(false);

            // when
            gitHubPushService.pushSolutionToRepo(ctx);

            // then
            then(eventService).should(never()).publishGitPushStatus(any(GitPushStatusEvent.class));
            then(userGithubService).should(never()).getUserGithubInfoById(anyLong());
            then(gitHubClient).should(never()).commitAndPushToRepo(any());
            then(exceptionNotifier).should(never()).notifyException(anyString(), any());
        }

        @Test
        @DisplayName("오답 제출 -> 아무 동작도 하지 않음")
        void noPushWhenNotPassed() {

            // given
            given(ctx.isGitPushStatus()).willReturn(true);
            given(ctx.isPassed()).willReturn(false);

            // when
            gitHubPushService.pushSolutionToRepo(ctx);

            // then
            then(eventService).should(never()).publishGitPushStatus(any(GitPushStatusEvent.class));
            then(userGithubService).should(never()).getUserGithubInfoById(anyLong());
            then(gitHubClient).should(never()).commitAndPushToRepo(any());
            then(exceptionNotifier).should(never()).notifyException(anyString(), any());
        }

        @Test
        @DisplayName("코드 변경 없음 -> 푸시 생략")
        void noPushWhenCodeNotChanged() throws Exception {

            // given
            given(ctx.isGitPushStatus()).willReturn(true);
            given(ctx.isPassed()).willReturn(true);

            given(ctx.getSessionKey()).willReturn(sessionKey);
            given(ctx.getUserId()).willReturn(userId);

            given(userGithubService.getUserGithubInfoById(userId)).willReturn(info);
            given(info.getGithubAccessToken()).willReturn(encryptedToken);

            given(aesUtil.decrypt(encryptedToken)).willReturn(decryptedToken);

            try (var ms = mockStatic(GitHubPushRequest.class)) {
                ms.when(() -> GitHubPushRequest.of(ctx, info, decryptedToken)).thenReturn(req);

                given(gitHubClient.isSourceCodeNewOrChanged(req)).willReturn(false);

                // when
                gitHubPushService.pushSolutionToRepo(ctx);

                // then
                then(gitHubClient).should(never()).commitAndPushToRepo(any());
                then(eventService).should(never()).publishGitPushStatus(GitPushStatusEvent.started(ctx));
                then(eventService).should(never()).publishGitPushStatus(GitPushStatusEvent.succeeded(ctx));
                then(exceptionNotifier).should(never()).notifyException(anyString(), any());
            }
        }

        @Test
        @DisplayName("활성화 & 정답 제출 -> 메서드 실행 및 시작 & 성공 이벤트 발행")
        void pushSuccess() throws Exception {

            // given
            given(ctx.isGitPushStatus()).willReturn(true);
            given(ctx.isPassed()).willReturn(true);

            given(ctx.getSessionKey()).willReturn(sessionKey);
            given(ctx.getUserId()).willReturn(userId);

            given(userGithubService.getUserGithubInfoById(userId)).willReturn(info);
            given(info.getGithubAccessToken()).willReturn(encryptedToken);
            given(aesUtil.decrypt(encryptedToken)).willReturn(decryptedToken);

            // when & then
            try (var ms = mockStatic(GitHubPushRequest.class)) {
                ms.when(() -> GitHubPushRequest.of(ctx, info, decryptedToken)).thenReturn(req);

                given(gitHubClient.isSourceCodeNewOrChanged(req)).willReturn(true);

                gitHubPushService.pushSolutionToRepo(ctx);

                then(eventService).should().publishGitPushStatus(GitPushStatusEvent.started(ctx));
                then(gitHubClient).should().commitAndPushToRepo(req);
                then(eventService).should().publishGitPushStatus(GitPushStatusEvent.succeeded(ctx));
                then(exceptionNotifier).should(never()).notifyException(anyString(), any());
            }
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @Test
        @DisplayName("Git Push 실패 -> 알림 호출 & 실패 이벤트 발행")
        void pushFailure() throws Exception {

            // given
            given(ctx.isGitPushStatus()).willReturn(true);
            given(ctx.isPassed()).willReturn(true);
            given(ctx.getSessionKey()).willReturn(sessionKey);
            given(ctx.getUserId()).willReturn(userId);
            given(userGithubService.getUserGithubInfoById(userId)).willReturn(info);
            given(info.getGithubAccessToken()).willReturn(encryptedToken);
            given(aesUtil.decrypt(encryptedToken)).willReturn(decryptedToken);

            willThrow(SubmissionException.class).given(gitHubClient).commitAndPushToRepo(req);

            // when & then
            try (var ms = mockStatic(GitHubPushRequest.class)) {
                ms.when(() -> GitHubPushRequest.of(ctx, info, decryptedToken))
                    .thenReturn(req);

                given(gitHubClient.isSourceCodeNewOrChanged(req)).willReturn(true);

                gitHubPushService.pushSolutionToRepo(ctx);

                then(eventService).should().publishGitPushStatus(GitPushStatusEvent.started(ctx));
                then(exceptionNotifier).should().notifyException(eq("commitAndPush"), any(SubmissionException.class));
                then(eventService).should().publishGitPushStatus(GitPushStatusEvent.failed(ctx));
            }
        }
    }
}
