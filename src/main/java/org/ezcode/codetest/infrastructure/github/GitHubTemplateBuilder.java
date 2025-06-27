package org.ezcode.codetest.infrastructure.github;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.infrastructure.github.model.FileType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubTemplateBuilder {

    private final GitBlobCalculator gitBlobCalculator;

    protected List<Map<String, Object>> buildGitTreeEntries(
        GitHubPushRequest req, String codeBlobSha
    ) {
        String mdContent = buildMarkdown(req);
        String mdBlobSha = gitBlobCalculator.calculateBlobSha(mdContent);

        Map<String, String> blobMap = Map.of(
            FileType.README.name(), mdBlobSha,
            FileType.SOURCE.name(), codeBlobSha
        );

        return buildGitTreeEntriesFromMap(req, blobMap);
    }

    private String buildMarkdown(GitHubPushRequest req) {
        return """
            # %s. %s
            - 제출 일자: %s
            
            %s
            
            
            ### 제출 요약
            - 메모리: %sKB
            - 실행 시간: %sms
            
            > 사이트: Ezcode
            """.formatted(
            req.problemId(),
            req.problemTitle(),
            req.submittedAt(),
            req.problemDescription(),
            req.averageMemoryUsage(),
            req.averageExecutionTime()
        );
    }

    private List<Map<String, Object>> buildGitTreeEntriesFromMap(
        GitHubPushRequest req,
        Map<String, String> blobShaMap
    ) {
        return blobShaMap.entrySet().stream()
            .map(entry -> {
                FileType fileType = FileType.valueOf(entry.getKey());
                String path = String.format("ezcode/%s/%s",
                    req.difficulty(), fileType.resolveFilename(req)
                );
                return Map.<String, Object>of(
                    "path", path,
                    "mode", "100644",
                    "type", "blob",
                    "sha", entry.getValue()
                );
            })
            .collect(Collectors.toList());
    }
}
