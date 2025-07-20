package org.ezcode.codetest.application.submission.dto.request.compile;

import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record CodeCompileRequest(

    String source_code,

    Long language_id,

    String stdin

) {
    public static CodeCompileRequest of(int index, SubmissionContext ctx) {
        return new CodeCompileRequest(
            ctx.getSourceCode(),
            ctx.getJudge0Id(),
            ctx.getInput(index));
    }

}
