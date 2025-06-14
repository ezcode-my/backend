package org.ezcode.codetest.presentation.game.management;

import org.ezcode.codetest.application.game.dto.request.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.SkillSaveRequest;
import org.ezcode.codetest.application.game.management.GameAdminUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class ItemManagementController {

	private final GameAdminUseCase gameAdminUseCase;

	@PostMapping("/items")
	public ResponseEntity<Void> createItem(
		@RequestBody ItemSaveRequest request
	) {
		gameAdminUseCase.createItem(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/items")
	public ResponseEntity<Void> deleteItem(
		@RequestBody ItemDeleteRequest request
	) {
		gameAdminUseCase.deleteItem(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/skills")
	public ResponseEntity<Void> createSkill(
		@RequestBody SkillSaveRequest request
	) {
		gameAdminUseCase.createSkill(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/skills")
	public ResponseEntity<Void> deleteSkill(
		@RequestBody SkillDeleteRequest request
	) {
		gameAdminUseCase.deleteSkill(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
