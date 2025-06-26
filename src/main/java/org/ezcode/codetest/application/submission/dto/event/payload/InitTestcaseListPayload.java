package org.ezcode.codetest.application.submission.dto.event.payload;

import java.util.List;
import java.util.stream.IntStream;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record InitTestcaseListPayload(

    int seqId,

    String input,

    String expectedOutput,

    String status

) {
    public static List<InitTestcaseListPayload> from(SubmissionContext ctx) {
        return IntStream.rangeClosed(1, ctx.getTestcaseCount())
            .mapToObj(seq -> {
                Testcase tc = ctx.getTestcases().get(seq - 1);
                return new InitTestcaseListPayload(
                    seq,
                    tc.getInput(),
                    tc.getOutput(),
                    "채점 중"
                );
            })
            .toList();
    }
}
