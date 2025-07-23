package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;

public interface GitHubClient {

    boolean isSourceCodeNewOrChanged(GitHubPushRequest req);

    void commitAndPushToRepo(GitHubPushRequest req);
}
