package org.ezcode.codetest.application.submission.service;

import java.util.List;
import java.util.UUID;

import org.ezcode.codetest.application.submission.aop.CodeReviewLock;
import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.LockManager;
import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.infrastructure.event.dto.submission.SubmissionMessage;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
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

    private final ReviewClient reviewClient;
    private final UserDomainService userDomainService;
    private final ProblemDomainService problemDomainService;
    private final LanguageDomainService languageDomainService;
    private final SubmissionDomainService submissionDomainService;
    private final QueueProducer queueProducer;
    private final ExceptionNotifier exceptionNotifier;
    private final LockManager lockManager;
    private final JudgementService judgementService;

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

            SubmissionContext ctx = createSubmissionContext(msg);
            judgementService.publishInitTestcases(ctx);
            judgementService.runTestcases(ctx);
            judgementService.finalizeAndPublish(ctx);
        } catch (Exception e) {
            judgementService.publishSubmissionError(msg.sessionKey(), e);
            exceptionNotifier.notifyException("submitCodeStream", e);
        } finally {
            lockManager.releaseLock("submission", msg.userId(), msg.problemId());
        }
    }

    private SubmissionContext createSubmissionContext(SubmissionMessage msg) {
        User user = userDomainService.getUserById(msg.userId());
        Language language = languageDomainService.getLanguage(msg.languageId());
        ProblemInfo problemInfo = problemDomainService.getProblemInfo(msg.problemId());

        return SubmissionContext.initialize(user, language, problemInfo, msg);
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
}
