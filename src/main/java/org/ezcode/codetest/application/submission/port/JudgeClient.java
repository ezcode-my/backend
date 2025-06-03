package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.application.submission.dto.request.CompileRequest;
import org.ezcode.codetest.application.submission.model.JudgeResult;

public interface JudgeClient {
	JudgeResult execute(CompileRequest request);
}
