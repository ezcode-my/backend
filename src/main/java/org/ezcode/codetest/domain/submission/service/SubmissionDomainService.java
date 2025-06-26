package org.ezcode.codetest.domain.submission.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.submission.model.SubmissionResult;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;
import org.ezcode.codetest.domain.submission.dto.SubmissionData;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.submission.repository.SubmissionRepository;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionDomainService {

    private final SubmissionRepository submissionRepository;
    private final UserProblemResultRepository userProblemResultRepository;

    public SubmissionResult finalizeSubmission(SubmissionContext ctx) {

        createSubmission(SubmissionData.toEntity(ctx));

        boolean allPassed = (ctx.getPassedCount() == ctx.getTestcaseCount());

        return getUserProblemResult(ctx.user().getId(), ctx.getProblem().getId()).map(
                result -> {
                    if (!result.isCorrect() && allPassed) {
                        modifyUserProblemResult(result, true);
                        return SubmissionResult.from(result, false);
                    }
                    return SubmissionResult.from(result, true);
                })
            .orElseGet(() -> SubmissionResult.from(createUserProblemResult(
                    UserProblemResult.builder()
                        .user(ctx.user())
                        .problem(ctx.getProblem())
                        .isCorrect(allPassed)
                        .build()
                ),false)
            );
    }

    public boolean handleEvaluationAndUpdateStats(
        TestcaseEvaluationInput input, SubmissionContext ctx
    ) {
        boolean isPassed = evaluate(input);

        if (isPassed) {
            ctx.incrementPassedCount();
        } else {
            ctx.updateMessage(input.resultMessage());
        }
        ctx.incrementProcessedCount();

        collectStatistics(ctx.aggregator(), input);

        return isPassed;
    }

    public List<Submission> getSubmissions(Long userId) {
        return submissionRepository.findSubmissionsByUserId(userId);
    }

    public List<WeeklySolveCount> getWeeklySolveCounts(
        LocalDateTime startDateTime, LocalDateTime endDateTime
    ) {
        return submissionRepository.fetchWeeklySolveCounts(startDateTime, endDateTime);
    }

    private boolean evaluate(TestcaseEvaluationInput input) {
        boolean isCorrect = input.isCorrect();
        boolean timeEfficient = input.timeEfficient();
        boolean memoryEfficient = input.memoryEfficient();
        AnswerEvaluation answerEvaluation = new AnswerEvaluation(isCorrect, timeEfficient, memoryEfficient);
        return answerEvaluation.isPassed();
    }

    private void collectStatistics(SubmissionAggregator aggregator, TestcaseEvaluationInput input) {
        aggregator.accumulate(input);
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

    private void modifyUserProblemResult(UserProblemResult userProblemResult, boolean isCorrect) {
        userProblemResultRepository.updateUserProblemResult(userProblemResult, isCorrect);
    }
}
