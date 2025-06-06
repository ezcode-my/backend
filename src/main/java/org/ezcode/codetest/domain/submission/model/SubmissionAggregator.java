package org.ezcode.codetest.domain.submission.model;

public class SubmissionAggregator {

	private double totalExecutionTime;

	private long totalMemoryUsage;

	private int count;

	public void accumulate(double executionTime, long memoryUsage) {
		totalExecutionTime += executionTime;
		totalMemoryUsage += memoryUsage;
		count++;
	}

	public double averageExecutionTime() {
		return count == 0 ? 0 : Math.round(totalExecutionTime / count * 1000.0) / 1000.0;
	}

	public long averageMemoryUsage() {
		return count == 0 ? 0 : totalMemoryUsage / count;
	}
}
