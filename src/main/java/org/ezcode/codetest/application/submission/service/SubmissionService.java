package org.ezcode.codetest.application.submission.service;

import org.ezcode.codetest.common.model.TempProblemInfo;
import org.ezcode.codetest.application.submission.dto.request.CompileRequest;
import org.ezcode.codetest.application.submission.dto.request.SubmitRequest;
import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.application.submission.dto.response.SubmitResponse;
import org.ezcode.codetest.application.submission.port.JudgeClient;
import org.ezcode.codetest.domain.problem.model.AnswerEvaluation;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.service.LanguageDomainService;
import org.ezcode.codetest.domain.problem.service.ProblemDomainService;
import org.ezcode.codetest.domain.problem.service.SubmissionDomainService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionService {

	private final JudgeClient judgeClient;
	private final ProblemDomainService problemDomainService;
	private final LanguageDomainService languageDomainService;
	private final SubmissionDomainService submissionDomainService;

	public SubmitResponse submitCode(Long problemId, SubmitRequest request) {

		// ProblemInfo problemInfo = problemDomainService.getProblemInfo(problemId);
		Language language = languageDomainService.getLanguage(request.languageId());
		JudgeResult result = judgeClient.execute(
			CompileRequest.of(request.sourceCode(), language.getJudge0Id(), "2 10")
		);

		TempProblemInfo tempProblemInfo = new TempProblemInfo("12");
		AnswerEvaluation eval = submissionDomainService.evaluate(
			tempProblemInfo.output(),
			result.output(),
			result.success()
		);

		return SubmitResponse.builder()
			.isCorrect(eval.isCorrect())
			.expectedOutput(eval.expectedOutput())
			.actualOutput(eval.actualOutput())
			.time(result.time())
			.memory(result.memory())
			.message(result.message())
			.build();
	}
}
