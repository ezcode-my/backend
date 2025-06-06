package org.ezcode.codetest.application.submission.dto.response.compile;

public record ExecutionResultResponse(

	String stdout,

	Double time,

	Long memory,

	String stderr,

	String token,

	String compile_output,

	int exit_code,

	ExecutionStatus status

) {

	public long getMemory() {
		return this.memory == null ? 0L : memory;
	}

	public double getTime() {
		return this.time == null ? 0.0 : time;
	}

	public record ExecutionStatus(

		int id,

		String description

	) {}
}
