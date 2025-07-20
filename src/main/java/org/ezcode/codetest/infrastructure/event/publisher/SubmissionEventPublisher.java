package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.application.submission.dto.event.GitPushStatusEvent;
import org.ezcode.codetest.application.submission.dto.event.ProblemCountAdjustmentEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionErrorEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;
import org.ezcode.codetest.application.submission.port.SubmissionEventService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionEventPublisher implements SubmissionEventService {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publishInitTestcases(TestcaseListInitializedEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishTestcaseUpdate(TestcaseEvaluatedEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishFinalResult(SubmissionJudgingFinishedEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishSubmissionError(SubmissionErrorEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishGitPushStatus(GitPushStatusEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishProblemCountAdjustment(ProblemCountAdjustmentEvent event) {
        publisher.publishEvent(event);
    }

}
