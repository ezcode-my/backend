package org.ezcode.codetest.application.submission.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.ezcode.codetest.application.submission.dto.event.ProblemCountAdjustmentEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionErrorEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.dto.event.payload.InitTestcaseListPayload;
import org.ezcode.codetest.application.submission.dto.event.payload.TestcaseResultPayload;
import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.application.submission.port.ProblemEventService;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.model.SubmissionResult;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JudgementService {

    private final SubmissionDomainService submissionDomainService;
    private final SubmissionEventService submissionEventService;
    private final ProblemEventService problemEventService;
    private final JudgeClient judgeClient;
    private final Executor judgeTestcaseExecutor;
    private final ExceptionNotifier exceptionNotifier;

    public void publishInitTestcases(SubmissionContext ctx) {
        submissionEventService.publishInitTestcases(
            TestcaseListInitializedEvent.of(ctx, InitTestcaseListPayload.from(ctx))
        );
    }

    public void runTestcases(SubmissionContext ctx) throws InterruptedException {

        IntStream.range(0, ctx.getTestcaseCount())
            .forEach(i -> runTestcaseAsync(i, ctx));

        if (!ctx.latch().await(100, TimeUnit.SECONDS)) {
            throw new SubmissionException(SubmissionExceptionCode.TESTCASE_TIMEOUT);
        }
    }

    @Transactional
    public void finalizeAndPublish(SubmissionContext ctx) {
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        publishFinalResult(ctx);
        publishProblemSolve(submissionResult);
        publishProblemCountAdjustment(ctx, submissionResult);
    }

    public void publishSubmissionError(String sessionKey, Exception e) {
        submissionEventService.publishSubmissionError(new SubmissionErrorEvent(sessionKey, e));
    }

    private void runTestcaseAsync(int index, SubmissionContext ctx) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("[Judge RUN] Thread = {}", Thread.currentThread().getName());
                String token = judgeClient.submitAndGetToken(CodeCompileRequest.of(index, ctx));
                JudgeResult result = judgeClient.pollUntilDone(token);

                TestcaseEvaluationInput input = TestcaseEvaluationInput.from(result, ctx, index);

                boolean isPassed = submissionDomainService.handleEvaluationAndUpdateStats(input, ctx);

                publishTestcaseUpdate(index, ctx, isPassed, result);
            } catch (Exception e) {
                if (ctx.notified().compareAndSet(false, true)) {
                    publishSubmissionError(ctx.getSessionKey(), e);
                    exceptionNotifier.notifyException("runTestcaseAsync", e);
                }
            } finally {
                ctx.countDown();
            }
        }, judgeTestcaseExecutor);
    }

    private void publishTestcaseUpdate(int index, SubmissionContext ctx, boolean isPassed, JudgeResult result) {
        submissionEventService.publishTestcaseUpdate(TestcaseEvaluatedEvent.of(
            ctx, TestcaseResultPayload.fromEvaluation(ctx.getTestcaseId(index), isPassed, result))
        );
    }

    private void publishFinalResult(SubmissionContext ctx){
        submissionEventService.publishFinalResult(SubmissionJudgingFinishedEvent.from(ctx));
    }

    private void publishProblemSolve(SubmissionResult submissionResult) {
        problemEventService.publishProblemSolveEvent(submissionResult);
    }

    private void publishProblemCountAdjustment(SubmissionContext ctx, SubmissionResult submissionResult) {
        submissionEventService.publishProblemCountAdjustment(
            new ProblemCountAdjustmentEvent(ctx.getProblemId(), submissionResult.isSolved())
        );
    }
}
