package org.ezcode.codetest.application.submission.dto.request;

import lombok.Builder;

@Builder
public record CompileRequest(

	String source_code,

	Long language_id,

	String stdin

) {
	public static CompileRequest of(String sourceCode, Long languageId, String stdin) {
		return CompileRequest.builder()
			.source_code(sourceCode)
			.language_id(languageId)
			.stdin(stdin)
			.build();
	}
}
