package org.ezcode.codetest.infrastructure.github;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.application.submission.port.GitHubClient;
import org.ezcode.codetest.common.security.util.AESUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GitHubApiClient implements GitHubClient {

    private final AESUtil aesUtil;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public void commitAndPushToRepo(GitHubPushRequest request) {

        try {
            String owner = request.owner();
            String repo = request.repo();
            String branch = request.branch();
            String accessToken = aesUtil.decrypt(request.accessToken());
            Long problemId = request.problemId();
            String problemTitle = request.problemTitle();
            String problemDescription = request.problemDescription();
            Long averageMemoryUsage = request.averageMemoryUsage();
            Long averageExecutionTime = request.averageExecutionTime();
            String sourceCode = request.sourceCode();
            String submittedAt = request.submittedAt();

            String mdPath = "test/ezcode/README.md";
            String codePath = String.format("test/ezcode/%s.java", problemTitle);
            String commitMsg = String.format("Add solution for %s", problemId);

            WebClient webClient = webClientBuilder
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .build();

            String newSha = calculateBlobSha(sourceCode);
            Optional<String> existingSha = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/repos/{owner}/{repo}/contents/{path}")
                    .queryParam("ref", branch)
                    .build(owner, repo, codePath)
                )
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(node -> node.get("sha").asText())
                .onErrorResume(err -> Mono.empty())
                .blockOptional();

            if (existingSha.filter(sha -> sha.equals(newSha)).isPresent()) {
                return;
            }

            String md =
                """
                    # 문제 풀이 상세
                    - 사이트: Ezcode
                    
                    ## 문제: %s
                    ### 문제 설명
                    %s
                    
                    ## 제출 요약
                    - 메모리: %sKB
                    - 실행 시간: %sms
                    - 제출 일자: %s
                    """.formatted(problemTitle, problemDescription, averageMemoryUsage, averageExecutionTime,
                    submittedAt);
            String code = String.format("%s", sourceCode);

            ObjectNode mdBody = objectMapper.createObjectNode();
            mdBody.put("message", commitMsg);
            mdBody.put("branch", branch);
            mdBody.put("content", encodeBase64(md));
            existingSha.ifPresent(sha -> mdBody.put("sha", sha));

            webClient.put()
                .uri("/repos/{owner}/{repo}/contents/{path}", owner, repo, mdPath)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mdBody)
                .retrieve()
                .toBodilessEntity()
                .block();

            ObjectNode codeBody = objectMapper.createObjectNode();
            codeBody.put("message", commitMsg);
            codeBody.put("branch", branch);
            codeBody.put("content", encodeBase64(code));
            existingSha.ifPresent(sha -> codeBody.put("sha", sha));
            webClient.put()
                .uri("/repos/{owner}/{repo}/contents/{path}", owner, repo, codePath)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(codeBody)
                .retrieve()
                .toBodilessEntity()
                .block();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String calculateBlobSha(String content) {
        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            String header = "blob " + contentBytes.length + "\0";
            byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);

            byte[] store = new byte[headerBytes.length + contentBytes.length];
            System.arraycopy(headerBytes, 0, store, 0, headerBytes.length);
            System.arraycopy(contentBytes, 0, store, headerBytes.length, contentBytes.length);

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] shaBytes = md.digest(store);

            StringBuilder sb = new StringBuilder();
            for (byte b : shaBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }

    public String encodeBase64(String utf8) {
        if (utf8 == null) return "";
        return Base64.getEncoder().encodeToString(utf8.getBytes(StandardCharsets.UTF_8));
    }
}
