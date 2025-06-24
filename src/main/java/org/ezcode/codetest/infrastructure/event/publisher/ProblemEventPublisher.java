package org.ezcode.codetest.infrastructure.event.publisher;

import org.ezcode.codetest.domain.submission.model.entity.UserProblemResult;
import org.ezcode.codetest.infrastructure.event.dto.GameLevelUpEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemEventPublisher {

	private final ApplicationEventPublisher publisher;

	public void publishProblemSolveEvent(UserProblemResult event) {

		Long userId = event.getUser().getId();
		boolean isCorrect = event.isCorrect();
		String problemCategory = event.getProblem().getCategory().getDescription();

		publisher.publishEvent(new GameLevelUpEvent(userId, isCorrect, problemCategory));
	}
}
