package org.ezcode.codetest.domain.submission;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.application.submission.model.SubmissionContext;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.domain.submission.model.SubmissionAggregator;
import org.ezcode.codetest.domain.submission.model.SubmissionResult;
import org.ezcode.codetest.domain.submission.model.TestcaseEvaluationInput;
import org.ezcode.codetest.domain.submission.model.entity.Submission;
import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.domain.submission.repository.SubmissionRepository;
import org.ezcode.codetest.domain.submission.repository.UserProblemResultRepository;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("제출 도메인 서비스 테스트")
public class SubmissionDomainServiceTest {

    @InjectMocks
    private SubmissionDomainService submissionDomainService;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private UserProblemResultRepository userProblemResultRepository;

    @Mock
    private SubmissionContext ctx;

    @Mock
    private UserProblemResult userProblemResult;

    @Mock
    private SubmissionAggregator aggregator;

    @Mock
    private User user;

    @Mock
    private Problem problem;

    @Mock
    private Language language;

    @Mock
    private TestcaseEvaluationInput input;

    private Long userId;
    private Long problemId;
    private Long averageExecutionTime;
    private Long averageMemoryUsage;
    private String sourceCode;
    private String currentMessage;
    private int passedCount;
    private int testcaseCount;

    @BeforeEach
    void setup() {
        userId = 1L;
        problemId = 1L;
        averageExecutionTime = 1000L;
        averageMemoryUsage = 12000L;
        sourceCode = "source-code";
        currentMessage = "current-message";
        passedCount = 10;
        testcaseCount = 10;
    }

    @Test
    @DisplayName("처음 제출 -> 새로 저장하고 정답이면 correct count 증가")
    void firstSubmission_passed() {

        // given
        given(ctx.user()).willReturn(user);
        given(user.getId()).willReturn(userId);

        given(ctx.getProblem()).willReturn(problem);
        given(problem.getId()).willReturn(problemId);

        given(ctx.language()).willReturn(language);
        given(ctx.getSourceCode()).willReturn(sourceCode);
        given(ctx.getCurrentMessage()).willReturn(currentMessage);
        given(ctx.getPassedCount()).willReturn(passedCount);
        given(ctx.getTestcaseCount()).willReturn(testcaseCount);

        given(ctx.aggregator()).willReturn(aggregator);
        given(aggregator.averageExecutionTime()).willReturn(averageExecutionTime);
        given(aggregator.averageMemoryUsage()).willReturn(averageMemoryUsage);

        given(ctx.getCategories()).willReturn(List.of());

        given(ctx.isPassed()).willReturn(true);

        given(userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId))
            .willReturn(Optional.empty());
        given(userProblemResultRepository.saveUserProblemResult(any())).willReturn(userProblemResult);
        given(userProblemResult.getUser()).willReturn(user);

        // when
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        // then
        then(submissionRepository).should().saveSubmission(any());
        then(ctx).should().incrementTotalSubmissions();
        then(ctx).should().incrementCorrectSubmissions();
        then(userProblemResultRepository).should().saveUserProblemResult(any());
        assertThat(submissionResult.isSolved()).isFalse();
    }

    @Test
    @DisplayName("처음 제출 -> 새로 저장하고 오답이면 correct count 미증가")
    void firstSubmission_failed() {

        // given
        given(ctx.user()).willReturn(user);
        given(user.getId()).willReturn(userId);

        given(ctx.getProblem()).willReturn(problem);
        given(problem.getId()).willReturn(problemId);

        given(ctx.language()).willReturn(language);
        given(ctx.getSourceCode()).willReturn(sourceCode);
        given(ctx.getCurrentMessage()).willReturn(currentMessage);
        given(ctx.getPassedCount()).willReturn(passedCount);
        given(ctx.getTestcaseCount()).willReturn(testcaseCount);

        given(ctx.aggregator()).willReturn(aggregator);
        given(aggregator.averageExecutionTime()).willReturn(averageExecutionTime);
        given(aggregator.averageMemoryUsage()).willReturn(averageMemoryUsage);

        given(ctx.getCategories()).willReturn(List.of());

        given(ctx.isPassed()).willReturn(false);

        given(userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId))
            .willReturn(Optional.empty());
        given(userProblemResultRepository.saveUserProblemResult(any())).willReturn(userProblemResult);
        given(userProblemResult.getUser()).willReturn(user);

        // when
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        // then
        then(submissionRepository).should().saveSubmission(any());
        then(ctx).should().incrementTotalSubmissions();
        then(ctx).should(never()).incrementCorrectSubmissions();
        then(userProblemResultRepository).should().saveUserProblemResult(any());
        assertThat(submissionResult.isSolved()).isFalse();
    }

    @Test
    @DisplayName("이전 오답 기록 -> 정답이면 update & correct count 증가")
    void retryAfterWrong_passed() {

        // given
        given(ctx.user()).willReturn(user);
        given(user.getId()).willReturn(userId);

        given(ctx.getProblem()).willReturn(problem);
        given(problem.getId()).willReturn(problemId);

        given(ctx.language()).willReturn(language);
        given(ctx.getSourceCode()).willReturn(sourceCode);
        given(ctx.getCurrentMessage()).willReturn(currentMessage);
        given(ctx.getPassedCount()).willReturn(passedCount);
        given(ctx.getTestcaseCount()).willReturn(testcaseCount);

        given(ctx.aggregator()).willReturn(aggregator);
        given(aggregator.averageExecutionTime()).willReturn(averageExecutionTime);
        given(aggregator.averageMemoryUsage()).willReturn(averageMemoryUsage);

        given(ctx.getCategories()).willReturn(List.of());

        given(ctx.isPassed()).willReturn(true);

        given(userProblemResult.isCorrect()).willReturn(false);
        given(userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId))
            .willReturn(Optional.of(userProblemResult));
        given(userProblemResult.getUser()).willReturn(user);

        // when
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        // then
        then(submissionRepository).should().saveSubmission(any());
        then(ctx).should().incrementTotalSubmissions();
        then(ctx).should().incrementCorrectSubmissions();
        then(userProblemResultRepository).should().updateUserProblemResult(userProblemResult, true);
        assertThat(submissionResult.isSolved()).isFalse();
    }

    @Test
    @DisplayName("이전 오답 기록 -> 재제출 오답 시 update 미호출 & correct count 미증가")
    void retryAfterWrong_failedAgain() {

        // given
        given(ctx.user()).willReturn(user);
        given(user.getId()).willReturn(userId);

        given(ctx.getProblem()).willReturn(problem);
        given(problem.getId()).willReturn(problemId);

        given(ctx.language()).willReturn(language);
        given(ctx.getSourceCode()).willReturn(sourceCode);
        given(ctx.getCurrentMessage()).willReturn(currentMessage);
        given(ctx.getPassedCount()).willReturn(passedCount);
        given(ctx.getTestcaseCount()).willReturn(testcaseCount);

        given(ctx.aggregator()).willReturn(aggregator);
        given(aggregator.averageExecutionTime()).willReturn(averageExecutionTime);
        given(aggregator.averageMemoryUsage()).willReturn(averageMemoryUsage);

        given(ctx.getCategories()).willReturn(List.of());

        given(ctx.isPassed()).willReturn(false);

        given(userProblemResult.isCorrect()).willReturn(false);
        given(userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId))
            .willReturn(Optional.of(userProblemResult));
        given(userProblemResult.getUser()).willReturn(user);

        // when
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        // then
        then(submissionRepository).should().saveSubmission(any());
        then(ctx).should().incrementTotalSubmissions();
        then(ctx).should(never()).incrementCorrectSubmissions();
        then(userProblemResultRepository).should(never())
            .updateUserProblemResult(any(UserProblemResult.class), anyBoolean());
        assertThat(submissionResult.isSolved()).isFalse();
    }

    @Test
    @DisplayName("이전 정답 기록 -> 재제출 정답 시 update 미호출 & correct count 미증가")
    void retryAfterCorrect_passedAgain() {

        // given
        given(ctx.user()).willReturn(user);
        given(user.getId()).willReturn(userId);

        given(ctx.getProblem()).willReturn(problem);
        given(problem.getId()).willReturn(problemId);

        given(ctx.language()).willReturn(language);
        given(ctx.getSourceCode()).willReturn(sourceCode);
        given(ctx.getCurrentMessage()).willReturn(currentMessage);
        given(ctx.getPassedCount()).willReturn(passedCount);
        given(ctx.getTestcaseCount()).willReturn(testcaseCount);

        given(ctx.aggregator()).willReturn(aggregator);
        given(aggregator.averageExecutionTime()).willReturn(averageExecutionTime);
        given(aggregator.averageMemoryUsage()).willReturn(averageMemoryUsage);

        given(ctx.getCategories()).willReturn(List.of());

        given(ctx.isPassed()).willReturn(true);

        given(userProblemResult.isCorrect()).willReturn(true);
        given(userProblemResultRepository.findUserProblemResultByUserIdAndProblemId(userId, problemId))
            .willReturn(Optional.of(userProblemResult));
        given(userProblemResult.getUser()).willReturn(user);

        // when
        SubmissionResult submissionResult = submissionDomainService.finalizeSubmission(ctx);

        // then
        then(submissionRepository).should().saveSubmission(any());
        then(ctx).should().incrementTotalSubmissions();
        then(ctx).should(never()).incrementCorrectSubmissions();
        then(userProblemResultRepository).should(never())
            .updateUserProblemResult(any(UserProblemResult.class), anyBoolean());
        assertThat(submissionResult.isSolved()).isTrue();
    }

    @Test
    @DisplayName("통과된 테스트케이스일 때")
    void whenPassed() {

        // given
        given(ctx.aggregator()).willReturn(aggregator);
        given(input.isCorrect()).willReturn(true);
        given(input.timeEfficient()).willReturn(true);
        given(input.memoryEfficient()).willReturn(true);

        // when
        boolean passed = submissionDomainService.handleEvaluationAndUpdateStats(input, ctx);

        // then
        assertThat(passed).isTrue();
        then(ctx).should().incrementPassedCount();
        then(ctx).should().incrementProcessedCount();
        then(aggregator).should().accumulate(input);
        then(ctx).should(never()).updateMessage(any());
    }

    @Test
    @DisplayName("실패한 테스트케이스일 때")
    void whenFailed() {

        // given
        given(ctx.aggregator()).willReturn(aggregator);
        given(input.isCorrect()).willReturn(true);
        given(input.timeEfficient()).willReturn(false);
        given(input.memoryEfficient()).willReturn(true);
        given(input.resultMessage()).willReturn(currentMessage);

        // when
        boolean passed = submissionDomainService.handleEvaluationAndUpdateStats(input, ctx);

        // then
        assertThat(passed).isFalse();
        then(ctx).should(never()).incrementPassedCount();
        then(ctx).should().incrementProcessedCount();
        then(aggregator).should().accumulate(input);
        then(ctx).should().updateMessage(currentMessage);
    }

    @Test
    @DisplayName("userId에 맞는 제출 목록을 레포지토리에서 가져와 반환")
    void getSubmissions_returnListFromRepository() {

        // given
        Submission s1 = mock(Submission.class);
        Submission s2 = mock(Submission.class);
        List<Submission> mockList = List.of(s1, s2);

        given(submissionRepository.findSubmissionsByUserId(userId)).willReturn(mockList);

        // when
        List<Submission> result = submissionDomainService.getSubmissions(userId);

        // then
        then(submissionRepository).should().findSubmissionsByUserId(userId);
        assertThat(result).isSameAs(mockList);
    }

    @Test
    @DisplayName("기간에 따른 주간 통계 목록을 레포지토리에서 가져와 반환")
    void getWeeklySolveCounts_returnListFromRepository() {

        // given
        LocalDateTime start = LocalDateTime.of(2025, 6, 23, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 29, 23, 59);

        WeeklySolveCount w1 = mock(WeeklySolveCount.class);
        WeeklySolveCount w2 = mock(WeeklySolveCount.class);
        List<WeeklySolveCount> mockList = List.of(w1, w2);

        given(submissionRepository.fetchWeeklySolveCounts(start, end)).willReturn(mockList);

        // when
        List<WeeklySolveCount> result = submissionDomainService.getWeeklySolveCounts(start, end);

        // then
        then(submissionRepository).should().fetchWeeklySolveCounts(start, end);
        assertThat(result).isSameAs(mockList);
    }
}
