package org.ezcode.codetest.infrastructure.github;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;
import org.ezcode.codetest.infrastructure.github.model.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubContentBuilder {

    @Value("${github.repo.root-folder}")
    private String repoRootFolder;

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
            - 제출 언어: %s
            - 제출 일자: %s
            
            %s
            
            
            ### 제출 요약
            - 메모리: %sKB
            - 실행 시간: %sms
            
            > EzCode
            """.formatted(
            req.problemId(),
            req.problemTitle(),
            req.getLanguage(),
            formatSubmittedAt(req.submittedAt()),
            req.problemDescription(),
            req.averageMemoryUsage(),
            req.averageExecutionTime()
        );
    }

    private List<Map<String, Object>> buildGitTreeEntriesFromMap(
        GitHubPushRequest req,
        Map<String, String> blobShaMap
    ) {
        return blobShaMap.keySet().stream()
            .map(s -> {
                FileType fileType = FileType.valueOf(s);
                String path = String.format("%s/%s/%s/%s",
                    repoRootFolder, req.difficulty(), req.problemId(), fileType.resolveFilename(req)
                );

                String content = fileType == FileType.SOURCE
                    ? req.sourceCode()
                    : buildMarkdown(req);

                return Map.<String, Object>of(
                    "path", path,
                    "mode", "100644",
                    "type", "blob",
                    "content", content,
                    "encoding", "utf-8"
                );
            })
            .collect(Collectors.toList());
    }

    private String formatSubmittedAt(String timestamp) {
        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dt = LocalDateTime.parse(timestamp, inputFmt);
        return dt.format(outputFmt);
    }
}
