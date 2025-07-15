package org.ezcode.codetest.application.submission.dto.event;

public record ProblemCountAdjustmentEvent(

    Long problemId,

    boolean isSolved

) {
}
