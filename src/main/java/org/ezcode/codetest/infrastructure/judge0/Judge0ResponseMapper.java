package org.ezcode.codetest.infrastructure.judge0;

import org.ezcode.codetest.application.submission.dto.response.compile.ExecutionResultResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.springframework.stereotype.Component;

@Component
public class Judge0ResponseMapper {

    public JudgeResult toJudgeResult(ExecutionResultResponse executionResultResponse) {
        String output = extractActualOutput(executionResultResponse);
        boolean success = isSuccessful(executionResultResponse);
        return JudgeResult.builder()
            .actualOutput(output)
            .executionTime(toMilliseconds(executionResultResponse.getTime()))
            .memoryUsage(executionResultResponse.getMemory())
            .success(success)
            .message(executionResultResponse.status().description())
            .build();
    }

    private String extractActualOutput(ExecutionResultResponse executionResultResponse) {
        if (executionResultResponse.stdout() != null) {
            return normalizeOutput(executionResultResponse.stdout());
        }

        if (executionResultResponse.compile_output() != null) {
            return executionResultResponse.compile_output();
        }

        if (executionResultResponse.stderr() != null) {
            return executionResultResponse.stderr();
        }

        return "(No output)";
    }

    private boolean isSuccessful(ExecutionResultResponse executionResultResponse) {
        return executionResultResponse.stdout() != null && executionResultResponse.status().id() == 3;
    }

    private long toMilliseconds(double timeInSeconds) {
        return Math.round(timeInSeconds * 1000);
    }

    private String normalizeOutput(String output) {
        return output
            .replaceAll("[\\r\\n]+$", "")
            .strip();
    }
}
