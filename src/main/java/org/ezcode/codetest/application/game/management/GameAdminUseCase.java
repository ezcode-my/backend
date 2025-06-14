package org.ezcode.codetest.application.game.management;

import org.ezcode.codetest.application.game.dto.request.ItemSaveRequest;
import org.ezcode.codetest.domain.game.service.ItemManagementDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameAdminUseCase {

	private final ItemManagementDomainService itemManagementService;

	@Transactional
	public void createItem(ItemSaveRequest request) {

		itemManagementService.createItem(request.toItem());
	}

}
