package org.ezcode.codetest.application.submission.service;

import java.util.List;
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

	public SseEmitter enqueueCodeSubmission(Long problemId, CodeSubmitRequest request, AuthUser authUser) {
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
        String emitterKey = authUser.getId() + "_" + UUID.randomUUID();

		emitter.onCompletion(() -> log.info("[SSE 완료] 정상 종료됨"));
		emitter.onTimeout(() -> {
			log.warn("[SSE 타임아웃] 연결 시간이 초과되었습니다");
			emitter.completeWithError(new SubmissionException(SubmissionExceptionCode.EMITTER_SEND_ERROR));
			emitterStore.remove(emitterKey);
		});
		emitter.onError(e -> {
			log.error("[SSE 에러 발생] 예외: {}", e.toString(), e);
			emitterStore.remove(emitterKey);
		});

        emitterStore.save(emitterKey, emitter);

        queueProducer.enqueue(
			new SubmissionMessage(emitterKey, problemId, request.languageId(), authUser.getId(), request.sourceCode())
		);

        return emitter;
    }

	@Async("judgeSubmissionExecutor")
    public void submitCodeStream(SubmissionMessage msg) {
		try {
            log.info("[Submission RUN] Thread = {}", Thread.currentThread().getName());
			User user = userDomainService.getUserById(msg.userId());
            Language language = languageDomainService.getLanguage(msg.languageId());
            ProblemInfo problemInfo = problemDomainService.getProblemInfo(msg.problemId());
            SseEmitter emitter = emitterStore.get(msg.emitterKey()).orElseThrow(
                    () -> new SubmissionException(SubmissionExceptionCode.EMITTER_NOT_FOUND)
            );

			int totalTestcaseCount = problemInfo.getTestcaseCount();
            SubmissionContext context = SubmissionContext.initialize(totalTestcaseCount);

            for (Testcase tc : problemInfo.testcaseList()) {
				CompletableFuture.runAsync(() -> {
					try {
						log.info("[Judge RUN] Thread = {}", Thread.currentThread().getName());
						String token = judgeClient.submitAndGetToken(
								new CodeCompileRequest(msg.sourceCode(), language.getJudge0Id(), tc.getInput())
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

			if (!context.latch().await(60, TimeUnit.SECONDS)) {
				throw new SubmissionException(SubmissionExceptionCode.TESTCASE_TIMEOUT);
			}

			emitter.send(SseEmitter.event()
				.name("final")
				.data(context.toFinalResult(totalTestcaseCount)));
            emitter.complete();
            emitterStore.remove(msg.emitterKey());

            SubmissionData submissionData = SubmissionData.base(
            	user, problemInfo, language, msg.sourceCode(), context.getCurrentMessage()
            );

            submissionDomainService.finalizeSubmission(
				submissionData, context.aggregator(), context.getPassedCount()
			);
		} catch (Exception e) {
			emitterStore.get(msg.emitterKey()).ifPresent(emitter -> emitter.completeWithError(e));
			emitterStore.remove(msg.emitterKey());
			exceptionNotificationHelper(e);
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

	private void exceptionNotificationHelper(Exception e) {
		if (e instanceof SubmissionException se) {
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
				""".formatted(e.getMessage()),
				"submitCodeStream"
			);
		}

	}
}
