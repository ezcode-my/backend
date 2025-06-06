package org.ezcode.codetest.application.submission.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.ezcode.codetest.application.submission.dto.request.review.CodeReviewRequest;
import org.ezcode.codetest.application.submission.dto.request.review.ReviewPayload;
import org.ezcode.codetest.application.submission.dto.response.review.CodeReviewResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.FinalResultResponse;
import org.ezcode.codetest.application.submission.dto.response.submission.GroupedSubmissionResponse;
import org.ezcode.codetest.application.submission.model.ReviewResult;
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
import org.ezcode.codetest.domain.submission.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Testcase;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionService {

	private final JudgeClient judgeClient;
	private final ReviewClient reviewClient;
	private final UserDomainService userDomainService;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;
	private final SubmissionDomainService submissionDomainService;

	public SseEmitter submitCodeStream(Long problemId, CodeSubmitRequest request, AuthUser authUser) {

		SseEmitter emitter = new SseEmitter();

		new Thread(() -> {
			try {
				SubmissionAggregator aggregator = new SubmissionAggregator();
				User user = userDomainService.getUserById(authUser.getId());
				Language language = languageDomainService.getLanguage(request.languageId());
				ProblemInfo problemInfo = problemDomainService.getProblemInfo(problemId);

				int passedCount = 0;
				String message = "Accepted";

				for (Testcase tc : problemInfo.testcaseList()) {

					JudgeResult result = judgeClient.execute(
						new CodeCompileRequest(request.sourceCode(), language.getJudge0Id(), tc.getInput())
					);

					AnswerEvaluation evaluation = submissionDomainService.evaluate(
						tc.getOutput(), result.actualOutput(), result.success(), result.executionTime(), result.memoryUsage() , problemInfo
					);

					if (evaluation.isPassed()) {
						passedCount++;
					} else {
						message = result.message();
					}

					submissionDomainService.collectStatistics(aggregator, result.executionTime(), result.memoryUsage());

					emitter.send(JudgeResultResponse.fromEvaluation(result, evaluation));
				}

				SubmissionData submissionData = SubmissionData.base(
					user, problemInfo, language, request.sourceCode(), message
				);

				submissionDomainService.finalizeSubmission(submissionData, aggregator, passedCount);

				emitter.send(SseEmitter.event()
					.name("final")
					.data(new FinalResultResponse(problemInfo.getTestcaseCount(), passedCount, message))
				);

				emitter.complete();
			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		}).start();

		return emitter;
	}

	@Transactional(readOnly = true)
	public List<GroupedSubmissionResponse> getSubmissions(AuthUser authUser) {

		User user = userDomainService.getUserById(authUser.getId());

		return submissionDomainService.getSubmissions(user.getId()).stream()
			.collect(Collectors.groupingBy(Submission::getProblem))
			.entrySet()
			.stream()
			.map(entry -> {
				Problem problem = entry.getKey();
				List<Submission> sorted = entry.getValue().stream()
					.sorted(Comparator.comparing(Submission::getCreatedAt).reversed())
					.toList();
				return new GroupedSubmissionResponse(problem, sorted);
			})
			.toList();
	}

	public CodeReviewResponse getCodeReview(Long problemId, CodeReviewRequest request) {

		Problem problem = problemDomainService.getProblem(problemId);
		Language language = languageDomainService.getLanguage(request.languageId());

		ReviewResult reviewResult = reviewClient.requestReview(ReviewPayload.of(problem, language, request));

		return new CodeReviewResponse(reviewResult.reviewContent());
	}
}
