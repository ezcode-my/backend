package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.application.submission.port.ProblemEventService;
import org.ezcode.codetest.domain.submission.model.SubmissionResult;
import org.ezcode.codetest.infrastructure.event.dto.GameLevelUpEvent;
import org.springframework.context.ApplicationEventPublisher;
import java.util.List;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemEventPublisher implements ProblemEventService {

	private final ApplicationEventPublisher publisher;

	public void publishProblemSolveEvent(SubmissionResult event) {

		if(event.hasBeenSolved()) return;

		Long userId = event.userId();
		List<String> problemCategory = event.problemCategory();
		boolean isSolved = event.isSolved();

		publisher.publishEvent(new GameLevelUpEvent(userId, isSolved, problemCategory));
	}
}
