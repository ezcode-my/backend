package org.ezcode.codetest.application.submission.dto.request.compile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.ezcode.codetest.application.submission.model.SubmissionContext;

public record CodeCompileRequest(

    String source_code,

    Long language_id,

    String stdin

) {
    public static CodeCompileRequest of(int seqId, SubmissionContext ctx) {
        return new CodeCompileRequest(
            ctx.getSourceCode(),
            ctx.getJudge0Id(),
            ctx.getInput(seqId));
    }

    private static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
}
