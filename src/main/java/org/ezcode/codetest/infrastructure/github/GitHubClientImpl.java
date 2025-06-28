package org.ezcode.codetest.infrastructure.github;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.infrastructure.github.model.CommitContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GitHubClientImpl implements GitHubClient {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubContentBuilder templateBuilder;
    private final GitBlobCalculator blobCalculator;

    @Override
    public void commitAndPushToRepo(GitHubPushRequest req) {
        String codeBlobSha = blobCalculator.calculateBlobSha(req.sourceCode());
        Optional<String> existingSha = gitHubApiClient.fetchSourceBlobSha(req);

        if (existingSha.map(codeBlobSha::equals).orElse(false)) {
            return;
        }

        List<Map<String, Object>> entries = templateBuilder.buildGitTreeEntries(req, codeBlobSha);

        CommitContext ctx = gitHubApiClient.fetchCommitContext(req);
        String newTreeSha = gitHubApiClient.createTree(req, ctx.baseTreeSha(), entries);

        if (newTreeSha.equals(ctx.baseTreeSha())) {
            return;
        }

        gitHubApiClient.commitAndPush(req, ctx.headCommitSha(), newTreeSha);
    }
}
