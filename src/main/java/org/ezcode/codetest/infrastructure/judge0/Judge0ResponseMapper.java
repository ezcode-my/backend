package org.ezcode.codetest.infrastructure.judge0;

import org.ezcode.codetest.application.submission.dto.response.CompileResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.springframework.stereotype.Component;

@Component
public class Judge0ResponseMapper {

	public JudgeResult toJudgeResult(CompileResponse compileResponse) {
		String output = extractActualOutput(compileResponse);
		boolean success = isSuccessful(compileResponse);
		return JudgeResult.builder()
			.actualOutput(output)
			.executionTime(compileResponse.time())
			.memoryUsage(compileResponse.memory())
			.success(success)
			.message(compileResponse.status().description())
			.build();
	}

	private String extractActualOutput(CompileResponse compileResponse) {
		if (compileResponse.stdout() != null) return compileResponse.stdout();
		if (compileResponse.compile_output() != null) return compileResponse.compile_output();
		if (compileResponse.stderr() != null) return compileResponse.stderr();
		return "(No output)";
	}

	private boolean isSuccessful(CompileResponse compileResponse) {
		return compileResponse.stdout() != null && compileResponse.status().id() == 3;
	}
}
