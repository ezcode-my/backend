package org.ezcode.codetest.application.submission.service;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.service.UserGithubService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;
    private final UserGithubService userGithubService;

    public void commitAndPushToRepo(SubmissionContext ctx) {
        if (ctx.isGitPushStatus() && ctx.isPassed()) {
            UserGithubInfo info = userGithubService.getUserGithubInfoById(ctx.getUserId());
            gitHubClient.commitAndPushToRepo(GitHubPushRequest.of(ctx, info));
        }
    }
}
