package org.ezcode.codetest.domain.submission.model;

public enum SubmitStatus {
	IN_QUEUE(1, "In Queue"),
	PROCESSING(2, "Processing"),
	ACCEPTED(3, "Accepted"),
	WRONG_ANSWER(4, "Wrong Answer"),
	TIME_LIMIT_EXCEEDED(5, "Time Limit Exceeded"),
	COMPILATION_ERROR(6, "Compilation Error"),
	RUNTIME_ERROR_SIGSEGV(7, "Runtime Error (SIGSEGV)"),
	RUNTIME_ERROR_SIGXFSZ(8, "Runtime Error (SIGXFSZ)"),
	RUNTIME_ERROR_SIGFPE(9, "Runtime Error (SIGFPE)"),
	RUNTIME_ERROR_SIGABRT(10, "Runtime Error (SIGABRT)"),
	RUNTIME_ERROR_NZEC(11, "Runtime Error (NZEC)"),
	RUNTIME_ERROR_OTHER(12, "Runtime Error (Other)"),
	INTERNAL_ERROR(13, "Internal Error"),
	EXEC_FORMAT_ERROR(14, "Exec Format Error");

	private final int code;
	private final String description;

	SubmitStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static SubmitStatus fromCode(int code) {
		for (SubmitStatus status : values()) {
			if (status.code == code) return status;
		}
		throw new IllegalArgumentException("Invalid status code: " + code);
	}

}
