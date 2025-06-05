package org.ezcode.codetest.application.submission.service;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.response.SubmissionHistoryResponse;
import org.ezcode.codetest.domain.problem.model.dto.SubmissionData;
import org.ezcode.codetest.application.submission.dto.request.CompileRequest;
import org.ezcode.codetest.application.submission.dto.request.SubmitRequest;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.dto.response.SubmitResponse;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.domain.problem.model.dto.SubmitProcessResult;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.ezcode.codetest.domain.problem.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.problem.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionService {

	private final JudgeClient judgeClient;
	private final UserDomainService userDomainService;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;
	private final SubmissionDomainService submissionDomainService;

	@Transactional
	public SubmitResponse submitCode(Long problemId, SubmitRequest request, AuthUser authUser) {

		User user = userDomainService.getUserById(authUser.getId());
		Language language = languageDomainService.getLanguage(request.languageId());
		Problem problem = problemDomainService.getProblem(problemId);

		JudgeResult result = judgeClient.execute(
			CompileRequest.of(request.sourceCode(), language.getJudge0Id(), "2 10")
		);

		SubmissionData submissionData = SubmissionData.of(
			user, problem, language, request.sourceCode(),
			result.message(), result.executionTime(), result.memoryUsage()
		);

		SubmitProcessResult submitProcessResult = submissionDomainService.processSubmission(
			submissionData,
			result.actualOutput(),
			result.success()
		);

		return SubmitResponse.from(submitProcessResult);
	}

	public List<SubmissionHistoryResponse> getSubmissions(AuthUser authUser) {
		User user = userDomainService.getUserById(authUser.getId());

		List<Submission> submissions = submissionDomainService.getSubmissions(user.getId());

		return submissions.stream()
			.map(SubmissionHistoryResponse::from)
			.toList();
	}
}
