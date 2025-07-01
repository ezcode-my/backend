package org.ezcode.codetest.application.submission.service;

import org.ezcode.codetest.application.submission.dto.event.GitPushStatusEvent;
import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.common.security.util.AESUtil;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.service.UserGithubService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubPushService {

    private final GitHubClient gitHubClient;
    private final UserGithubService userGithubService;
    private final ExceptionNotifier exceptionNotifier;
    private final SubmissionEventService eventService;
    private final AESUtil aesUtil;

    public void pushSolutionToRepo(SubmissionContext ctx) {
        if (!ctx.isGitPushStatus() || !ctx.isPassed()) {
            return;
        }

        eventService.publishGitPushStatus(GitPushStatusEvent.started(ctx));
        UserGithubInfo info = userGithubService.getUserGithubInfoById(ctx.getUserId());

        try {
            String decryptedToken = aesUtil.decrypt(info.getGithubAccessToken());
            gitHubClient.commitAndPushToRepo(GitHubPushRequest.of(ctx, info, decryptedToken));
            eventService.publishGitPushStatus(GitPushStatusEvent.succeeded(ctx));
        } catch (Exception e) {
            exceptionNotifier.notifyException("commitAndPush", e);
            eventService.publishGitPushStatus(GitPushStatusEvent.failed(ctx));
        }
    }
}
