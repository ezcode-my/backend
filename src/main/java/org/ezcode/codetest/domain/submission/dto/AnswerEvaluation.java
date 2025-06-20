package org.ezcode.codetest.domain.submission.dto;

import lombok.Builder;

@Builder
public record AnswerEvaluation(

    boolean isCorrect,

    boolean timeEfficient,

    boolean memoryEfficient,

    String expectedOutput,

    String actualOutput

) {
    public boolean isPassed() {
        return this.isCorrect() && this.timeEfficient() && this.memoryEfficient();
    }
}
