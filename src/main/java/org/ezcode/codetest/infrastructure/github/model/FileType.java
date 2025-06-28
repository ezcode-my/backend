package org.ezcode.codetest.infrastructure.github.model;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;

public enum FileType {
    SOURCE, README;

    public String resolveFilename(GitHubPushRequest req) {
        return switch (this) {
            case SOURCE -> String.format("%s.%s",
                req.problemTitle(), Extensions.getExtensionByLanguage(req.languageName())
            );
            case README -> String.format("README_%s.md",
                req.languageName().toLowerCase()
            );
        };
    }
}
