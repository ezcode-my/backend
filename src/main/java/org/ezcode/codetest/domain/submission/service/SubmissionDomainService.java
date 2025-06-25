package org.ezcode.codetest.domain.submission.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.problem.model.ProblemInfo;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
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
    public UserProblemResult finalizeSubmission(
        SubmissionData submissionData,
        SubmissionAggregator aggregator,
        int passedCount) {

        createSubmission(SubmissionData.toEntity(
                submissionData.withAggregatedStats(aggregator),
                passedCount
            )
        );

        boolean allPassed = passedCount == submissionData.getTestCaseSize();

        return getUserProblemResult(submissionData.getUserId(), submissionData.getProblemId()).map(
                result -> {
                    if (!result.isCorrect() && allPassed) {
                        modifyUserProblemResult(result, true);
                        return result;
                    }
                    return result;
                })
            .orElseGet(() -> createUserProblemResult(
                UserProblemResult.builder()
                    .user(submissionData.user())
                    .problem(submissionData.problem())
                    .isCorrect(allPassed)
                    .build()
            ));
    }

    public AnswerEvaluation handleEvaluationAndUpdateStats(
        TestcaseEvaluationInput input, ProblemInfo problemInfo, SubmissionContext context
    ) {
        AnswerEvaluation evaluation =
            evaluate(input.expectedOutput(), input.actualOutput(), input.success(),
                input.executionTime(), input.memoryUsage(), problemInfo);

        if (evaluation.isPassed()) {
            context.incrementPassedCount();
        } else {
            context.updateMessage(input.resultMessage());
        }
        context.incrementProcessedCount();

        collectStatistics(context.aggregator(), input.executionTime(), input.memoryUsage());

        return evaluation;
    }

    public List<Submission> getSubmissions(Long userId) {
        return submissionRepository.findSubmissionsByUserId(userId);
    }

    public List<WeeklySolveCount> getWeeklySolveCounts(
        LocalDateTime startDateTime, LocalDateTime endDateTime
    ) {
        return submissionRepository.fetchWeeklySolveCounts(startDateTime, endDateTime);
    }

    private AnswerEvaluation evaluate(
        String expectedOutput, String actualOutput, boolean success, long executionTime, long memoryUsage,
        ProblemInfo problemInfo
    ) {
        boolean isCorrect = success && expectedOutput.strip().equals(actualOutput.strip());
        boolean timeEfficient = executionTime <= problemInfo.getTimeLimit();
        boolean memoryEfficient = memoryUsage <= problemInfo.getMemoryLimit();
        return new AnswerEvaluation(isCorrect, timeEfficient, memoryEfficient);
    }

    private void collectStatistics(SubmissionAggregator aggregator, long executionTime, long memoryUsage) {
        aggregator.accumulate(executionTime, memoryUsage);
    }

    private void createSubmission(Submission submission) {
        submissionRepository.saveSubmission(submission);
    }

    private Optional<UserProblemResult> getUserProblemResult(Long userId, Long problemId) {
        return userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId);
    }

    private UserProblemResult createUserProblemResult(UserProblemResult userProblemResult) {
        return userProblemResultRepository.saveUserProblemResult(userProblemResult);
    }

    private UserProblemResult modifyUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect) {
        return userProblemResultRepository.updateUserProblemResult(userProblemResult, isCorrect);
    }
}
