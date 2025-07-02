package org.ezcode.codetest.application.submission.dto.request.github;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;

public record GitHubPushRequest(

    String owner,

    String repo,

    String branch,

    String accessToken,

    Long problemId,

    String difficulty,

    String problemTitle,

    String problemDescription,

    String languageName,

    String languageVersion,

    Long averageMemoryUsage,

    Long averageExecutionTime,

    String sourceCode,

    String submittedAt

) {
    public static GitHubPushRequest of(SubmissionContext ctx, UserGithubInfo info, String decryptedToken) {
        return new GitHubPushRequest(
            info.getOwner(),
            info.getRepo(),
            info.getBranch(),
            decryptedToken,
            ctx.getProblem().getId(),
            ctx.getProblem().getDifficulty().getDifficulty(),
            ctx.getProblem().getTitle(),
            ctx.getProblem().getDescription(),
            ctx.getLanguageName(),
            ctx.getLanguageVersion(),
            ctx.aggregator().averageMemoryUsage(),
            ctx.aggregator().averageExecutionTime(),
            ctx.getSourceCode(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        );
    }

    public String getLanguage() {
        return languageName + " " + languageVersion;
    }

    public String getProblemInfo() {
        return problemId + ". " + problemTitle;
    }
}
