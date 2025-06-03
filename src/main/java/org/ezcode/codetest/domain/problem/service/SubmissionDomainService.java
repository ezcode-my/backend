package org.ezcode.codetest.domain.problem.service;

import org.ezcode.codetest.domain.problem.model.AnswerEvaluation;
import org.ezcode.codetest.domain.problem.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionDomainService {

	private SubmissionRepository submissionRepository;

	public AnswerEvaluation evaluate(String expectedOutput, String actualOutput, boolean success) {
		boolean isCorrect = success && expectedOutput.equals(actualOutput);
		return  AnswerEvaluation.builder()
			.isCorrect(isCorrect)
			.expectedOutput(expectedOutput)
			.actualOutput(actualOutput)
			.build();
	}
}
