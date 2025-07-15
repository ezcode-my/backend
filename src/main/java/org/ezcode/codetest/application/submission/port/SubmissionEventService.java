package org.ezcode.codetest.application.submission.port;

import org.ezcode.codetest.application.submission.dto.event.GitPushStatusEvent;
import org.ezcode.codetest.application.submission.dto.event.ProblemCountAdjustmentEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionErrorEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;

public interface SubmissionEventService {

    void publishInitTestcases(TestcaseListInitializedEvent event);

    void publishTestcaseUpdate(TestcaseEvaluatedEvent event);

    void publishFinalResult(SubmissionJudgingFinishedEvent event);

    void publishSubmissionError(SubmissionErrorEvent event);

    void publishGitPushStatus(GitPushStatusEvent event);

    void publishProblemCountAdjustment(ProblemCountAdjustmentEvent event);
}
