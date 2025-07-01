package org.ezcode.codetest.infrastructure.event.listener;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.event.GitPushStatusEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionErrorEvent;
import org.ezcode.codetest.application.submission.dto.event.SubmissionJudgingFinishedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseListInitializedEvent;
import org.ezcode.codetest.application.submission.dto.event.TestcaseEvaluatedEvent;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.GitPushStatusResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.ErrorWsResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.SubmissionFinalResultResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.InitTestcaseListResponse;
import org.ezcode.codetest.infrastructure.event.dto.submission.response.JudgeResultResponse;
import org.ezcode.codetest.infrastructure.event.publisher.StompMessageService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionEventListener {

    private final StompMessageService messageService;

    @EventListener
    public void onTestcaseInit(TestcaseListInitializedEvent event) {
        List<InitTestcaseListResponse> wsDtos = InitTestcaseListResponse.mapToList(event.payload());
        messageService.sendInitTestcases(event.sessionKey(), event.principalName(), wsDtos);
    }

    @EventListener
    public void onTestcaseUpdate(TestcaseEvaluatedEvent event) {
        JudgeResultResponse wsDto = JudgeResultResponse.from(event.payload());
        messageService.sendTestcaseResultUpdate(event.sessionKey(), event.principalName(), wsDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSubmissionFinished(SubmissionJudgingFinishedEvent event) {
        SubmissionFinalResultResponse wsDto = SubmissionFinalResultResponse.from(event.payload());
        messageService.sendFinalResult(event.sessionKey(), event.principalName(), wsDto);
    }

    @EventListener
    public void onSubmissionError(SubmissionErrorEvent event) {
        ErrorWsResponse wsDto = ErrorWsResponse.from(event.code());
        messageService.sendError(event.sessionKey(), wsDto);
    }

    @EventListener
    public void onGitPushStatus(GitPushStatusEvent event) {
        GitPushStatusResponse wsDto = new GitPushStatusResponse(event.pushStatus());
        messageService.sendGitStatus(event.sessionKey(), event.principalName(), wsDto);
    }
}
