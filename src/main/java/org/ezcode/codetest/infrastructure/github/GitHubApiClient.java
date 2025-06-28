package org.ezcode.codetest.infrastructure.github;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.domain.submission.exception.GitHubClientException;
import org.ezcode.codetest.domain.submission.exception.code.GitHubExceptionCode;
import org.ezcode.codetest.infrastructure.github.model.CommitContext;
import org.ezcode.codetest.infrastructure.github.model.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GitHubApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${github.repo.root-folder}")
    private String repoRootFolder;

    public static final String REPO_ROOT = "/repos/{owner}/{repo}";
    public static final String CONTENTS_PATH = REPO_ROOT + "/contents/{path}";
    public static final String REF_PATH      = REPO_ROOT + "/git/refs/heads/{branch}";
    public static final String COMMITS_PATH  = REPO_ROOT + "/git/commits/{sha}";
    public static final String TREES_PATH    = REPO_ROOT + "/git/trees";
    public static final String COMMIT_PATH   = REPO_ROOT + "/git/commits";

    protected Optional<String> fetchSourceBlobSha(GitHubPushRequest req) {
        String fileName = FileType.SOURCE.resolveFilename(req);
        String path = String.format("%s/%s/%s/%s", repoRootFolder, req.difficulty(), req.problemId(), fileName);

        return baseBuilder(req.accessToken())
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(CONTENTS_PATH)
                .queryParam("ref", req.branch())
                .build(req.owner(), req.repo(), path)
            )
            .retrieve()
            .onStatus(status -> status == HttpStatus.UNAUTHORIZED,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.INVALID_ACCESS_TOKEN)))
            .onStatus(status -> status == HttpStatus.FORBIDDEN,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.PERMISSION_DENIED)))
            .onStatus(status -> status == HttpStatus.TOO_MANY_REQUESTS,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.RATE_LIMIT_EXCEEDED)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .bodyToMono(JsonNode.class)
            .map(node -> node.get("sha").asText())
            .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty())
            .onErrorMap(e -> e instanceof GitHubClientException
                ? e
                : new GitHubClientException(GitHubExceptionCode.UNKNOWN_ERROR))
            .blockOptional();
    }

    protected CommitContext fetchCommitContext(GitHubPushRequest req) {
        String headCommitSha = fetchHeadCommitSha(req);
        String baseTreeSha = fetchBaseTreeSha(req, headCommitSha);
        return new CommitContext(headCommitSha, baseTreeSha);
    }

    protected String createTree(
        GitHubPushRequest req, String baseTreeSha, List<Map<String, Object>> entries
    ) {
        Map<String, Object> body = Map.of(
            "base_tree", baseTreeSha,
            "tree", entries
        );

        JsonNode treeRes = baseBuilder(req.accessToken())
            .post()
            .uri(TREES_PATH, req.owner(), req.repo())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.TREE_CREATION_FAILED)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .bodyToMono(JsonNode.class)
            .block();

        return Objects.requireNonNull(treeRes).get("sha").asText();
    }

    protected void commitAndPush(GitHubPushRequest req, String parentSha, String treeSha) {

        String commitSha = createCommit(req, parentSha, treeSha);
        updateBranchReference(req, commitSha);

    }

    private String fetchHeadCommitSha(GitHubPushRequest req) {

        JsonNode ref = baseBuilder(req.accessToken())
            .get()
            .uri(REF_PATH,
                req.owner(), req.repo(), req.branch())
            .retrieve()
            .onStatus(s -> s == HttpStatus.NOT_FOUND,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.BRANCH_NOT_FOUND)))
            .onStatus(s -> s == HttpStatus.UNAUTHORIZED,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.INVALID_ACCESS_TOKEN)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .bodyToMono(JsonNode.class)
            .block();

        return Objects.requireNonNull(ref)
            .get("object")
            .get("sha")
            .asText();
    }

    private String fetchBaseTreeSha(GitHubPushRequest req, String commitSha) {

        JsonNode commit = baseBuilder(req.accessToken())
            .get()
            .uri(COMMITS_PATH,
                req.owner(), req.repo(), commitSha)
            .retrieve()
            .onStatus(s -> s == HttpStatus.NOT_FOUND,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.REPOSITORY_NOT_FOUND)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .bodyToMono(JsonNode.class)
            .block();

        return Objects.requireNonNull(commit)
            .get("tree")
            .get("sha")
            .asText();

    }

    private String createCommit(GitHubPushRequest req, String parentSha, String treeSha) {
        Map<String, Object> body = Map.of(
            "message", String.format("문제 %s. %s (%s) – 메모리: %sKB, 시간: %sms",
                req.problemId(),
                req.problemTitle(),
                req.difficulty(),
                req.averageMemoryUsage(),
                req.averageExecutionTime()
            ),
            "tree", treeSha,
            "parents", List.of(parentSha)
        );

        JsonNode commitRes = baseBuilder(req.accessToken())
            .post()
            .uri(COMMIT_PATH, req.owner(), req.repo())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.COMMIT_CREATION_FAILED)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .bodyToMono(JsonNode.class)
            .block();

        return Objects.requireNonNull(commitRes).get("sha").asText();
    }

    private void updateBranchReference(GitHubPushRequest req, String newCommitSha) {
        Map<String, Object> body = Map.of("sha", newCommitSha);

        baseBuilder(req.accessToken())
            .patch()
            .uri(REF_PATH,
                req.owner(), req.repo(), req.branch())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.BRANCH_UPDATE_FAILED)))
            .onStatus(HttpStatusCode::is5xxServerError,
                resp -> Mono.error(new GitHubClientException(GitHubExceptionCode.NETWORK_ERROR)))
            .toBodilessEntity()
            .block();
    }

    private WebClient baseBuilder(String accessToken) {
        return webClientBuilder
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .build();
    }
}
