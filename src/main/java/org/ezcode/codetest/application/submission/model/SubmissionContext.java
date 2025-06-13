package org.ezcode.codetest.application.submission.model;

import org.ezcode.codetest.application.submission.dto.response.submission.FinalResultResponse;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record SubmissionContext(

    SubmissionAggregator aggregator,

    AtomicInteger passedCount,

    AtomicInteger processedCount,

    AtomicReference<String> message,

    CountDownLatch latch
) {
    public static SubmissionContext initialize(int totalTestcaseCount) {
        return new SubmissionContext(
                new SubmissionAggregator(),
                new AtomicInteger(0),
                new AtomicInteger(0),
                new AtomicReference<>("Accepted"),
                new CountDownLatch(totalTestcaseCount)
        );
    }

    public FinalResultResponse toFinalResult(int totalTestcaseCount) {
        return new FinalResultResponse(
                totalTestcaseCount,
                this.getProcessedCount(),
                this.getCurrentMessage()
        );
    }

    public void incrementPassedCount() {
        this.passedCount.incrementAndGet();
    }

    public void incrementProcessedCount() {
        this.processedCount.incrementAndGet();
    }

    public int getPassedCount() {
        return this.passedCount.get();
    }

    public int getProcessedCount() {
        return this.processedCount.get();
    }

    public String getCurrentMessage() {
        return this.message.get();
    }

    public void updateMessage(String message) {
        this.message.set(message);
    }

    public void countDown() {
        this.latch.countDown();
    }
}
