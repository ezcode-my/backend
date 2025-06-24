package org.ezcode.codetest.infrastructure.event.listener;

import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.infrastructure.event.dto.GameLevelUpEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameLevelUpListener {

	private final CharacterStatusDomainService characterDomainService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleGameCharacterLevelUp(GameLevelUpEvent event) {

		characterDomainService.gameCharacterLevelUp(event.userId(), event.isProblemSolved(), event.problemCategory());
	}

}
