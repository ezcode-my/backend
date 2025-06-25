package org.ezcode.codetest.domain.submission.model;

public class SubmissionAggregator {

    private long totalExecutionTime;

    private long totalMemoryUsage;

    private int count;

    public void accumulate(long executionTime, long memoryUsage) {
        totalExecutionTime += executionTime;
        totalMemoryUsage += memoryUsage;
        count++;
    }

    public long averageExecutionTime() {
        return count == 0 ? 0L : totalExecutionTime / count;
    }

    public long averageMemoryUsage() {
        return count == 0 ? 0L : totalMemoryUsage / count;
    }
}
