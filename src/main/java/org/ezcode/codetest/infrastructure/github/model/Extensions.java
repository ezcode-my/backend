package org.ezcode.codetest.infrastructure.github.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Extensions {

    JAVA("java"),
    C("c"),
    CPP("cpp"),
    PYTHON("py");

    private final String extension;

    public static String getExtensionByLanguage(String languageName) {
        try {
            return Extensions.valueOf(languageName.toUpperCase()).getExtension();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 언어입니다: " + languageName);
        }
    }
}
