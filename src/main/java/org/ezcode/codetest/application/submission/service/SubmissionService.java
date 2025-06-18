package org.ezcode.codetest.application.submission.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.application.submission.port.ExceptionNotifier;
import org.ezcode.codetest.application.submission.port.LockManager;
import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.domain.submission.exception.SubmissionException;
import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
import org.ezcode.codetest.infrastructure.event.dto.SubmissionMessage;
import org.ezcode.codetest.application.submission.port.EmitterStore;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.submission.dto.SubmissionData;
import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.dto.response.submission.JudgeResultResponse;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    private final EmitterStore emitterStore;
    private final QueueProducer queueProducer;
	private final Executor judgeTestcaseExecutor;
	private final ExceptionNotifier exceptionNotifier;
	private final LockManager lockManager;

	public SseEmitter enqueueCodeSubmission(Long problemId, CodeSubmitRequest request, AuthUser authUser) {

		boolean acquired = lockManager.tryLock(authUser.getId(), problemId);

		if (!acquired) {
			throw new SubmissionException(SubmissionExceptionCode.ALREADY_JUDGING);
		}

        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
        String emitterKey = authUser.getId() + "_" + UUID.randomUUID();

		log.info("[SSE 저장] emitterKey: {}", emitterKey);
		emitterStore.saveWithCallbacks(emitterKey, emitter);

        queueProducer.enqueue(
			new SubmissionMessage(emitterKey, problemId, request.languageId(), authUser.getId(), request.sourceCode())
		);

        return emitter;
    }

	@Async("judgeSubmissionExecutor")
    public void submitCodeStream(SubmissionMessage msg) {
		try {
            log.info("[Submission RUN] Thread = {}", Thread.currentThread().getName());
			log.info("[큐 수신] SubmissionMessage.emitterKey: {}", msg.emitterKey());
			User user = userDomainService.getUserById(msg.userId());
            Language language = languageDomainService.getLanguage(msg.languageId());
            ProblemInfo problemInfo = problemDomainService.getProblemInfo(msg.problemId());
            SseEmitter emitter = emitterStore.getOrElseThrow(msg.emitterKey());

			int totalTestcaseCount = problemInfo.getTestcaseCount();
            SubmissionContext context = SubmissionContext.initialize(totalTestcaseCount);

            for (Testcase tc : problemInfo.testcaseList()) {
				runTestcaseAsync(tc, msg, language.getJudge0Id(), problemInfo, context, emitter);
			}

			if (!context.latch().await(60, TimeUnit.SECONDS)) {
				emitter.completeWithError(new SubmissionException(SubmissionExceptionCode.TESTCASE_TIMEOUT));
				return;
			}

			emitter.send(SseEmitter.event()
				.name("final")
				.data(context.toFinalResult(totalTestcaseCount)));
			emitter.complete();

            SubmissionData submissionData = SubmissionData.base(
            	user, problemInfo, language, msg.sourceCode(), context.getCurrentMessage()
            );

            submissionDomainService.finalizeSubmission(
				submissionData, context.aggregator(), context.getPassedCount()
			);

		} catch (Exception e) {
			emitterStore.get(msg.emitterKey()).ifPresent(emitter -> emitter.completeWithError(e));
			exceptionNotificationHelper(e);
		} finally {
			emitterStore.remove(msg.emitterKey());
			lockManager.releaseLock(msg.userId(), msg.problemId());
		}
    }

    @Transactional(readOnly = true)
    public List<GroupedSubmissionResponse> getSubmissions(AuthUser authUser) {

        User user = userDomainService.getUserById(authUser.getId());
        List<Submission> submissions = submissionDomainService.getSubmissions(user.getId());
        return GroupedSubmissionResponse.groupByProblem(submissions);
    }

    public CodeReviewResponse getCodeReview(Long problemId, CodeReviewRequest request) {
        Problem problem = problemDomainService.getProblem(problemId);
        Language language = languageDomainService.getLanguage(request.languageId());

        ReviewResult reviewResult = reviewClient.requestReview(ReviewPayload.of(problem, language, request));

        return new CodeReviewResponse(reviewResult.reviewContent());
    }

	private void runTestcaseAsync(
		Testcase tc, SubmissionMessage msg, Long judge0Id,
		ProblemInfo problemInfo, SubmissionContext context, SseEmitter emitter
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
				emitter.send(JudgeResultResponse.fromEvaluation(result, evaluation));
			} catch (Exception e) {
				if (context.notified().compareAndSet(false, true)) {
					emitter.completeWithError(e);
					emitterStore.remove(msg.emitterKey());
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
