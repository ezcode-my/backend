package org.ezcode.codetest.domain.submission.dto;

import lombok.Builder;

@Builder
public record AnswerEvaluation(

    boolean isCorrect,

    boolean timeEfficient,

    boolean memoryEfficient

) {
    public boolean isPassed() {
        return isCorrect && timeEfficient && memoryEfficient;
    }
}
