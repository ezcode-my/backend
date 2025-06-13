package org.ezcode.codetest.presentation.game.management;

import org.ezcode.codetest.application.game.dto.request.ItemSaveRequest;
import org.ezcode.codetest.application.game.management.GameAdminUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games/items")
public class ItemManagementController {

	private final GameAdminUseCase gameAdminUseCase;

	@PostMapping
	public ResponseEntity<Void> createItem(
		@RequestBody ItemSaveRequest request
	) {

		gameAdminUseCase.createItem(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
