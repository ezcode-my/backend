package org.ezcode.codetest.application.submission.dto.response;

public record CompileResponse(

	String stdout,

	String time,

	Long memory,

	String stderr,

	String token,

	String compile_output,

	int exit_code,

	CompileStatus status

) {
	public record CompileStatus(

		int id,

		String description

	) {}
}
