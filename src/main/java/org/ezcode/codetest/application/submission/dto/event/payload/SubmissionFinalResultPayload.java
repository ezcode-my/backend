package org.ezcode.codetest.application.submission.dto.event.payload;

import lombok.Getter;

@Getter
public class SubmissionFinalResultPayload {

    private final int totalCount;

    private final int passedCount;

    private final boolean isCorrect;

    private final String message;

    public SubmissionFinalResultPayload(int totalCount, int passedCount, String message) {
        this.totalCount = totalCount;
        this.passedCount = passedCount;
        this.isCorrect = totalCount == passedCount;
        this.message = message;
    }
}
