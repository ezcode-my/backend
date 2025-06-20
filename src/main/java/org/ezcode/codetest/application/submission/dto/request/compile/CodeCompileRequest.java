package org.ezcode.codetest.application.submission.dto.request.compile;

public record CodeCompileRequest(

    String source_code,

    Long language_id,

    String stdin

) {
}
