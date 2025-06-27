package org.ezcode.codetest.application.submission.model;

import org.ezcode.codetest.application.submission.dto.event.payload.SubmissionFinalResultPayload;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record SubmissionContext(

    SubmissionAggregator aggregator,

    AtomicInteger passedCount,

    AtomicInteger processedCount,

    AtomicReference<String> message,

    CountDownLatch latch,

    AtomicBoolean notified,

    User user,

    Language language,

    ProblemInfo problemInfo,

    SubmissionMessage msg
) {
    public static SubmissionContext initialize(
        User user, Language language, ProblemInfo problemInfo, SubmissionMessage msg
    ) {
        return new SubmissionContext(
            new SubmissionAggregator(),
            new AtomicInteger(0),
            new AtomicInteger(0),
            new AtomicReference<>("Accepted"),
            new CountDownLatch(problemInfo.getTestcaseCount()),
            new AtomicBoolean(false),
            user,
            language,
            problemInfo,
            msg
        );
    }

    public SubmissionFinalResultPayload toFinalResult() {
        return new SubmissionFinalResultPayload(
            this.getTestcaseCount(),
            this.getPassedCount(),
            this.getCurrentMessage()
        );
    }

    public void incrementPassedCount() {
        passedCount.incrementAndGet();
    }

    public void incrementProcessedCount() {
        processedCount.incrementAndGet();
    }

    public int getPassedCount() {
        return passedCount.get();
    }

    public String getCurrentMessage() {
        return message.get();
    }

    public void updateMessage(String message) {
        this.message.set(message);
    }

    public void countDown() {
        latch.countDown();
    }

    public List<Testcase> getTestcases() {
        return problemInfo.testcaseList();
    }

    public int getTestcaseCount() {
        return problemInfo.getTestcaseCount();
    }

    public String getSourceCode() {
        return msg.sourceCode();
    }

    public long getJudge0Id() {
        return language.getJudge0Id();
    }

    public String getInput(int seqId) {
        return getTestcases().get(seqId - 1).getInput();
    }

    public String getExpectedOutput(int seqId) {
        return getTestcases().get(seqId - 1).getOutput();
    }

    public long getTimeLimit() {
        return problemInfo.getTimeLimit();
    }

    public long getMemoryLimit() {
        return problemInfo.getMemoryLimit();
    }

    public String getSessionKey() {
        return msg.sessionKey();
    }

    public Problem getProblem() {
        return problemInfo.problem();
    }

    public List<String> getCategories() {
        return problemInfo.categories();
    }

    public void incrementTotalSubmissions() {
        getProblem().incrementTotalSubmissions();
    }

    public void incrementCorrectSubmissions() {
        getProblem().incrementCorrectSubmissions();
    }

    public boolean isGitPushStatus() {
        return user.isGitPushStatus();
    }

    public Long getUserId() {
        return user.getId();
    }

    public boolean isPassed() {
        return getPassedCount() == getTestcaseCount();
    }

    public String getLanguageName() {
        return language.getName();
    }
}
