package org.ezcode.codetest.application.submission.dto.request.review;

import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;

public record ReviewPayload(

    String problemDescription,

    String languageName,

    String sourceCode,

    boolean isCorrect

) {
    public static ReviewPayload of(Problem problem, Language language, CodeReviewRequest request) {
        return new ReviewPayload(
            problem.getDescription(),
            language.getName(),
            request.sourceCode(),
            request.isCorrect()
        );
    }
}
