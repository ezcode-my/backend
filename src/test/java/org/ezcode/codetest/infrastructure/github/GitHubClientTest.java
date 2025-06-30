package org.ezcode.codetest.infrastructure.github;

import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.infrastructure.github.model.CommitContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GitHubClient 단위 테스트")
public class GitHubClientTest {

    @InjectMocks
    GitHubClientImpl gitHubClientImpl;

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private GitHubContentBuilder templateBuilder;

    @Mock
    private GitBlobCalculator blobCalculator;

    @Mock
    private GitHubPushRequest request;

    @Mock
    private CommitContext ctx;

    private String sourceCode;

    @BeforeEach
    void setup() {
        sourceCode = "source-code";
    }

    @Test
    @DisplayName("동일한 SHA -> 얼리 리턴")
    void blobUnchanged_earlyReturn() {

        // given
        String sha = "abc123";
        given(request.sourceCode()).willReturn(sourceCode);
        given(blobCalculator.calculateBlobSha(request.sourceCode())).willReturn(sha);
        given(gitHubApiClient.fetchSourceBlobSha(request)).willReturn(Optional.of(sha));

        // when
        gitHubClientImpl.commitAndPushToRepo(request);

        // then
        then(gitHubApiClient).should(never()).fetchCommitContext(any());
        then(templateBuilder).should(never()).buildGitTreeEntries(any(), any());
        then(gitHubApiClient).should(never()).createTree(any(), any(), anyList());
        then(gitHubApiClient).should(never()).commitAndPush(any(), any(), any());
    }

    @Test
    @DisplayName("새 SHA == 트리 SHA -> 얼리 리턴")
    void newBlobEqualsTreeBlob_earlyReturn() {

        // given
        String oldSha = "old";
        String newSha = "new";
        given(request.sourceCode()).willReturn(sourceCode);
        given(blobCalculator.calculateBlobSha(request.sourceCode())).willReturn(newSha);
        given(gitHubApiClient.fetchSourceBlobSha(request)).willReturn(Optional.of(oldSha));
        given(gitHubApiClient.fetchCommitContext(request)).willReturn(ctx);

        List<Map<String, Object>> entries = List.of(Map.of());
        given(templateBuilder.buildGitTreeEntries(request, newSha)).willReturn(entries);

        given(ctx.baseTreeSha()).willReturn("tree");
        given(gitHubApiClient.createTree(request, "tree", entries)).willReturn("tree");

        // when
        gitHubClientImpl.commitAndPushToRepo(request);

        // then
        then(gitHubApiClient).should().fetchCommitContext(request);
        then(templateBuilder).should().buildGitTreeEntries(request, newSha);
        then(gitHubApiClient).should().createTree(request, "tree", entries);
        then(gitHubApiClient).should(never()).commitAndPush(any(), any(), any());
    }

    @Test
    @DisplayName("SHA 변경 & 트리 SHA 변경 시 커밋 + 푸시")
    void blobAndTreeChanged_commitAndPush() {

        // given
        String oldSha = "old";
        String newSha = "new";
        given(blobCalculator.calculateBlobSha(request.sourceCode())).willReturn(newSha);
        given(gitHubApiClient.fetchSourceBlobSha(request)).willReturn(Optional.of(oldSha));
        given(gitHubApiClient.fetchCommitContext(request)).willReturn(ctx);

        List<Map<String,Object>> entries = List.of(Map.of());
        given(templateBuilder.buildGitTreeEntries(request, newSha)).willReturn(entries);

        given(ctx.baseTreeSha()).willReturn("tree");
        given(ctx.headCommitSha()).willReturn("head");
        String newTree = "newTree";
        given(gitHubApiClient.createTree(request, "tree", entries)).willReturn(newTree);

        // when
        gitHubClientImpl.commitAndPushToRepo(request);

        // then
        then(gitHubApiClient).should().commitAndPush(request, "head", newTree);
    }
}
