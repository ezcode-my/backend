package org.ezcode.codetest.application.submission.service;

import org.ezcode.codetest.application.submission.dto.event.GitStatusEvent;
import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
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

    public void commitAndPushToRepo(SubmissionContext ctx) {
        if (ctx.isGitPushStatus() && ctx.isPassed()) {
            try {
                eventService.publishGitStatusUpdate(GitStatusEvent.onStart(ctx.getSessionKey()));
                UserGithubInfo info = userGithubService.getUserGithubInfoById(ctx.getUserId());
                gitHubClient.commitAndPushToRepo(GitHubPushRequest.of(ctx, info));
                eventService.publishGitStatusUpdate(GitStatusEvent.onSuccess(ctx.getSessionKey()));
            } catch (Exception e) {
                exceptionNotifier.notifyException("commitAndPush", e);
                eventService.publishGitStatusUpdate(GitStatusEvent.onFailed(ctx.getSessionKey()));
            }
        }
    }
}
