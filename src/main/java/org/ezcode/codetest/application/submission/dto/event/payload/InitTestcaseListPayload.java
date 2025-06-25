package org.ezcode.codetest.application.submission.dto.event.payload;

import java.util.List;
import java.util.stream.IntStream;

import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;

public record InitTestcaseListPayload(

    int seqId,

    String input,

    String expectedOutput,

    String status

) {
    public static List<InitTestcaseListPayload> from(ProblemInfo info) {
        return IntStream.rangeClosed(1, info.getTestcaseCount())
            .mapToObj(seq -> {
                Testcase tc = info.testcaseList().get(seq - 1);
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
