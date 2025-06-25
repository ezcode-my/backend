package org.ezcode.codetest.application.submission.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.submission.aop.CodeReviewLock;
import org.ezcode.codetest.application.submission.dto.event.SubmissionErrorEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;
import org.ezcode.codetest.application.submission.dto.event.payload.InitTestcaseListPayload;
import org.ezcode.codetest.application.submission.dto.event.payload.TestcaseResultPayload;
import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.LockManager;
import org.ezcode.codetest.application.submission.port.ProblemEventService;
import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.submission.dto.SubmissionData;
import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final JudgeClient judgeClient;
    private final ReviewClient reviewClient;
    private final UserDomainService userDomainService;
    private final ProblemDomainService problemDomainService;
    private final LanguageDomainService languageDomainService;
    private final SubmissionDomainService submissionDomainService;
    private final QueueProducer queueProducer;
    private final Executor judgeTestcaseExecutor;
    private final ExceptionNotifier exceptionNotifier;
    private final LockManager lockManager;
    private final SubmissionEventService submissionEventService;
    private final ProblemEventService problemEventService;

    public String enqueueCodeSubmission(Long problemId, CodeSubmitRequest request, AuthUser authUser) {

        boolean acquired = lockManager.tryLock("submission", authUser.getId(), problemId);

        if (!acquired) {
            throw new SubmissionException(SubmissionExceptionCode.ALREADY_JUDGING);
        }

        String sessionKey = authUser.getId() + "_" + UUID.randomUUID();

        queueProducer.enqueue(
            new SubmissionMessage(sessionKey, problemId, request.languageId(), authUser.getId(), request.sourceCode())
        );

        return sessionKey;
    }

    @Async("judgeSubmissionExecutor")
    public void submitCodeStream(SubmissionMessage msg) {
        try {
            log.info("[Submission RUN] Thread = {}", Thread.currentThread().getName());
            log.info("[큐 수신] SubmissionMessage.sessionKey: {}", msg.sessionKey());
            User user = userDomainService.getUserById(msg.userId());
            Language language = languageDomainService.getLanguage(msg.languageId());
            ProblemInfo problemInfo = problemDomainService.getProblemInfo(msg.problemId());

            List<Testcase> testcaseList = problemInfo.testcaseList();
            int totalTestcaseCount = problemInfo.getTestcaseCount();

            SubmissionContext context = SubmissionContext.initialize(totalTestcaseCount);

            submissionEventService.publishInitTestcases(
                new TestcaseListInitializedEvent(msg.sessionKey(), InitTestcaseListPayload.from(problemInfo))
            );

            for (int i = 0; i < totalTestcaseCount; i++) {
                int seqId = i + 1;
                runTestcaseAsync(seqId, testcaseList.get(i), msg, language.getJudge0Id(), problemInfo, context);
            }

            if (!context.latch().await(100, TimeUnit.SECONDS)) {
                throw new SubmissionException(SubmissionExceptionCode.TESTCASE_TIMEOUT);
            }

            submissionEventService.publishFinalResult(
                new SubmissionJudgingFinishedEvent(msg.sessionKey(), context.toFinalResult(totalTestcaseCount))
            );

            SubmissionData submissionData = SubmissionData.base(
                user, problemInfo, language, msg.sourceCode(), context.getCurrentMessage()
            );

            UserProblemResult userProblemResult = submissionDomainService.finalizeSubmission(
                submissionData, context.aggregator(), context.getPassedCount()
            );

            problemEventService.publishProblemSolveEvent(userProblemResult);
        } catch (Exception e) {
            submissionEventService.publishSubmissionError(new SubmissionErrorEvent(msg.sessionKey(), e));
            exceptionNotificationHelper(e);
        } finally {
            lockManager.releaseLock("submission", msg.userId(), msg.problemId());
        }
    }

    @Transactional(readOnly = true)
    public List<GroupedSubmissionResponse> getSubmissions(AuthUser authUser) {

        User user = userDomainService.getUserById(authUser.getId());
        List<Submission> submissions = submissionDomainService.getSubmissions(user.getId());
        return GroupedSubmissionResponse.groupByProblem(submissions);
    }

    @Transactional
    @CodeReviewLock(prefix = "review")
    public CodeReviewResponse getCodeReview(Long problemId, CodeReviewRequest request, AuthUser authUser) {
        User user = userDomainService.getUserById(authUser.getId());
        userDomainService.decreaseReviewToken(user);

        Problem problem = problemDomainService.getProblem(problemId);
        Language language = languageDomainService.getLanguage(request.languageId());

        ReviewResult reviewResult = reviewClient.requestReview(ReviewPayload.of(problem, language, request));

        return new CodeReviewResponse(reviewResult.reviewContent());
    }

    private void runTestcaseAsync(
        int seqId, Testcase tc, SubmissionMessage msg,
        Long judge0Id, ProblemInfo problemInfo, SubmissionContext context
    ) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("[Judge RUN] Thread = {}", Thread.currentThread().getName());
                String token = judgeClient.submitAndGetToken(
                    new CodeCompileRequest(msg.sourceCode(), judge0Id, tc.getInput())
                );
                JudgeResult result = judgeClient.pollUntilDone(token);

                AnswerEvaluation evaluation = submissionDomainService.handleEvaluationAndUpdateStats(
                    TestcaseEvaluationInput.from(tc, result), problemInfo, context
                );

                submissionEventService.publishTestcaseUpdate(
                    new TestcaseEvaluatedEvent(msg.sessionKey(), TestcaseResultPayload.fromEvaluation(seqId, result, evaluation))
                );
            } catch (Exception e) {
                if (context.notified().compareAndSet(false, true)) {
                    submissionEventService.publishSubmissionError(new SubmissionErrorEvent(msg.sessionKey(), e));
                    exceptionNotificationHelper(e);
                }
            } finally {
                context.countDown();
            }
        }, judgeTestcaseExecutor);
    }

    private void exceptionNotificationHelper(Throwable t) {
        if (t instanceof SubmissionException se) {
            var code = se.getResponseCode();
            exceptionNotifier.sendEmbed(
                "채점 예외",
                "채점 중 SubmissionException 발생",
                """
                    • 성공 여부: %s
                    • 상태코드: %s
                    • 메시지: %s
                    """.formatted(code.isSuccess(), code.getStatus(), code.getMessage()),
                "submitCodeStream"
            );
        } else {
            exceptionNotifier.sendEmbed(
                "채점 예외",
                "채점 중 알 수 없는 예외 발생",
                """
                    	• 성공 여부: false
                    	• 상태코드: 500
                    	• 메시지: %s
                    """.formatted(Optional.ofNullable(t.getMessage()).orElse("No message")),
                "submitCodeStream"
            );
        }
    }
}
