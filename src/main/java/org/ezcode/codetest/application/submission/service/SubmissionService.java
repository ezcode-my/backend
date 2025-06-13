package org.ezcode.codetest.application.submission.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.FinalResultResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
import org.ezcode.codetest.application.submission.port.QueueProducer;
import org.ezcode.codetest.infrastructure.event.dto.SubmissionMessage;
import org.ezcode.codetest.application.submission.port.EmitterStore;
import org.ezcode.codetest.application.submission.port.ReviewClient;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.submission.dto.SubmissionData;
import org.ezcode.codetest.application.submission.dto.request.compile.CodeCompileRequest;
import org.ezcode.codetest.application.submission.dto.request.submission.CodeSubmitRequest;
import org.ezcode.codetest.application.submission.model.JudgeResult;
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
import org.ezcode.codetest.infrastructure.event.service.RedisJudgeQueueProducer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

	private static final String COMPILE_MESSAGE = "Accepted";
	private final ThreadPoolTaskExecutor judgeTestcaseExecutor;

	public SseEmitter enqueueCodeSubmission(Long problemId, CodeSubmitRequest request, AuthUser authUser) {
		log.info("[Submission START] Thread = {}", Thread.currentThread().getName());
		SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
		String emitterKey = authUser.getId() + "_" + UUID.randomUUID();

		emitterStore.save(emitterKey, emitter);

		SubmissionMessage message = new SubmissionMessage(
			emitterKey, problemId, request.languageId(), authUser.getId(), request.sourceCode()
		);

		queueProducer.enqueue(message);

		return emitter;
	}

	@Async("judgeSubmissionExecutor")
	public void submitCodeStreamTest(SubmissionMessage msg) {
		try {
			User user = userDomainService.getUserById(msg.userId());
			Language language = languageDomainService.getLanguage(msg.languageId());
			ProblemInfo problemInfo = problemDomainService.getProblemInfo(msg.problemId());

			SubmissionAggregator aggregator = new SubmissionAggregator();
			AtomicInteger passedCount = new AtomicInteger(0);
			AtomicInteger processedCount = new AtomicInteger(0);
			AtomicReference<String> message = new AtomicReference<>(COMPILE_MESSAGE);
			SseEmitter emitter = emitterStore.get(msg.emitterKey()).orElse(null);

			List<Testcase> testcases = problemInfo.testcaseList();

			for (Testcase tc : testcases) {
				CompletableFuture.runAsync(() -> {
					log.info("[Testcase RUN] Thread = {}", Thread.currentThread().getName());
					try {
						String token = judgeClient.submitAndGetToken(
							new CodeCompileRequest(msg.sourceCode(), language.getJudge0Id(), tc.getInput())
						);

						JudgeResult result = judgeClient.pollUntilDone(token);

						AnswerEvaluation evaluation = submissionDomainService.evaluate(
							tc.getOutput(), result.actualOutput(), result.success(),
							result.executionTime(), result.memoryUsage(), problemInfo
						);

						if (evaluation.isPassed()) {
							passedCount.incrementAndGet();
						} else {
							message.set(result.message());
						}
						processedCount.incrementAndGet();

						submissionDomainService.collectStatistics(aggregator, result.executionTime(), result.memoryUsage());

						if (emitter != null) {
							emitter.send(JudgeResultResponse.fromEvaluation(result, evaluation));
						}
					} catch (Exception e) {
						if (emitter != null) emitter.completeWithError(e);
					}
				}, judgeTestcaseExecutor);
			}
			CompletableFuture.runAsync(() -> {
				try {
					while (processedCount.get() < testcases.size()) {
						Thread.sleep(200); // 체크 간격
					}

					if (emitter != null) {
						emitter.send(SseEmitter.event()
							.name("final")
							.data(new FinalResultResponse(testcases.size(), passedCount.get(), message.get())));
						emitter.complete();
						emitterStore.remove(msg.emitterKey());
					}

					SubmissionData submissionData = SubmissionData.base(
						user, problemInfo, language, msg.sourceCode(), message.get()
					);
					submissionDomainService.finalizeSubmission(submissionData, aggregator, passedCount.get());

				} catch (Exception e) {
					if (emitter != null) emitter.completeWithError(e);
				}
			}, judgeTestcaseExecutor);

		} catch (Exception e) {
			emitterStore.get(msg.emitterKey()).ifPresent(emitter -> emitter.completeWithError(e));
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
}
