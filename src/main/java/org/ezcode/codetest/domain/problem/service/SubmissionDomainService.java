package org.ezcode.codetest.domain.problem.service;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.problem.model.dto.SubmissionData;
import org.ezcode.codetest.domain.problem.model.dto.SubmitProcessResult;
import org.ezcode.codetest.domain.problem.model.entity.Submission;
import org.ezcode.codetest.domain.problem.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.problem.repository.SubmissionRepository;
import org.ezcode.codetest.domain.problem.repository.UserProblemResultRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionDomainService {

	private final SubmissionRepository submissionRepository;
	private final UserProblemResultRepository userProblemResultRepository;

	public SubmitProcessResult processSubmission(
		SubmissionData submissionData,
		String actualOutput,
		boolean success
	) {
		AnswerEvaluation evaluation = evaluate("12", actualOutput, success);

		int testCasePassedCount = evaluation.isCorrect() ? 1 : 0;

		createSubmission(SubmissionData.toEntity(submissionData, testCasePassedCount));

		getUserProblemResult(submissionData.getUserId(), submissionData.getProblemId()).ifPresentOrElse(
			result -> {
				if (!result.isCorrect()) {
					modifyUserProblemResult(result, evaluation.isCorrect());
				}
			},
			() -> createUserProblemResult(
				UserProblemResult.builder()
					.user(submissionData.user())
					.problem(submissionData.problem())
					.isCorrect(evaluation.isCorrect())
					.build()
			)
		);

		return SubmitProcessResult.of(submissionData, evaluation);
	}

	public List<Submission> getSubmissions(Long userId) {
		return submissionRepository.findSubmissionsByUserId(userId);
	}

	private void createSubmission(Submission submission) {
		submissionRepository.saveSubmission(submission);
	}

	private Optional<UserProblemResult> getUserProblemResult(Long userId, Long problemId) {
		return userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId);
	}

	private void createUserProblemResult(UserProblemResult userProblemResult) {
		userProblemResultRepository.saveUserProblemResult(userProblemResult);
	}

	private void modifyUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect) {
		userProblemResultRepository.updateUserProblemResult(userProblemResult, isCorrect);
	}

	public AnswerEvaluation evaluate(String expectedOutput, String actualOutput, boolean success) {
		boolean isCorrect = success && expectedOutput.strip().equals(actualOutput.strip());
		return AnswerEvaluation.builder()
			.isCorrect(isCorrect)
			.expectedOutput(expectedOutput)
			.actualOutput(actualOutput)
			.build();
	}
}
