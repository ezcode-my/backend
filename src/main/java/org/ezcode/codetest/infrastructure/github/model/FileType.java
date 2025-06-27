package org.ezcode.codetest.infrastructure.github.model;

import org.ezcode.codetest.application.submission.dto.request.github.GitHubPushRequest;

public enum FileType {
    SOURCE, README;

    public String resolveFilename(GitHubPushRequest request) {
        return switch (this) {
            case SOURCE -> String.format("%s.%s",
                request.problemTitle(), Extensions.getExtensionByLanguage(request.languageName())
            );
            case README -> "README.md";
        };
    }
}
