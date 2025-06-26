package org.ezcode.codetest.infrastructure.event.listener;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.infrastructure.event.dto.GameLevelUpEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameLevelUpListener {

	private final CharacterStatusDomainService characterDomainService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleGameCharacterLevelUp(GameLevelUpEvent event) {

		try {
			characterDomainService.gameCharacterLevelUp(event.userId(), event.isSolved(),
				event.problemCategory());
		} catch (GameException ge) {
			log.info("현재 해당 사용자는 게임 캐릭터를 생성한 상태가 아닙니다. {}", ge.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} catch (Exception e) {
			log.warn("문제 풀이로 인한 레벨업 실패 Unknown Error 발생", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}
}
