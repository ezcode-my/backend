package org.ezcode.codetest.domain.submission.service;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.submission.dto.SubmissionData;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.submission.repository.SubmissionRepository;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionDomainService {

	private final SubmissionRepository submissionRepository;
	private final UserProblemResultRepository userProblemResultRepository;

	@Transactional
	public void finalizeSubmission(SubmissionData submissionData, SubmissionAggregator aggregator, int passedCount) {

		createSubmission(SubmissionData.toEntity(
				submissionData.withAggregatedStats(aggregator),
				passedCount
			)
		);

		boolean allPassed = passedCount == submissionData.getTestCaseSize();

		getUserProblemResult(submissionData.getUserId(), submissionData.getProblemId()).ifPresentOrElse(
			result -> {
				if (!result.isCorrect()) {
					modifyUserProblemResult(result, allPassed);
				}
			},
			() -> createUserProblemResult(
				UserProblemResult.builder()
					.user(submissionData.user())
					.problem(submissionData.problem())
					.isCorrect(allPassed)
					.build()
			)
		);
	}

	public AnswerEvaluation evaluate(
		String expectedOutput, String actualOutput, boolean success, double executionTime, long memoryUsage, ProblemInfo problemInfo
	) {
		boolean isCorrect = success && expectedOutput.strip().equals(actualOutput.strip());
		boolean timeEfficient = executionTime <= problemInfo.getTimeLimit();
		boolean memoryEfficient = memoryUsage <= problemInfo.getMemoryLimit();
		return new AnswerEvaluation(isCorrect, timeEfficient, memoryEfficient, expectedOutput, actualOutput);
	}

	public void collectStatistics(SubmissionAggregator aggregator, double executionTime, long memoryUsage) {
		aggregator.accumulate(executionTime, memoryUsage);
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
}
