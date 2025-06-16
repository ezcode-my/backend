package org.ezcode.codetest.presentation.game.management;

import org.ezcode.codetest.application.game.dto.request.EncounterChoiceDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.EncounterChoiceSaveRequest;
import org.ezcode.codetest.application.game.dto.request.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.RandomEncounterDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.RandomEncounterSaveRequest;
import org.ezcode.codetest.application.game.dto.request.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.SkillSaveRequest;
import org.ezcode.codetest.application.game.management.GameAdminUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameManagementController {

	private final GameAdminUseCase gameAdminUseCase;

	@PostMapping("/items")
	public ResponseEntity<Void> createItem(
		@RequestBody @Validated ItemSaveRequest request
	) {
		gameAdminUseCase.createItem(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/items")
	public ResponseEntity<Void> deleteItem(
		@RequestBody @Validated ItemDeleteRequest request
	) {
		gameAdminUseCase.deleteItem(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/skills")
	public ResponseEntity<Void> createSkill(
		@RequestBody @Validated SkillSaveRequest request
	) {
		gameAdminUseCase.createSkill(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/skills")
	public ResponseEntity<Void> deleteSkill(
		@RequestBody @Validated SkillDeleteRequest request
	) {
		gameAdminUseCase.deleteSkill(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/encounters")
	public ResponseEntity<Void> createRandomEncounter(
		@RequestBody @Validated RandomEncounterSaveRequest request
	) {
		gameAdminUseCase.createRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/encounters")
	public ResponseEntity<Void> deleteRandomEncounter(
		@RequestBody @Validated RandomEncounterDeleteRequest request
	) {
		gameAdminUseCase.deleteRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/choices")
	public ResponseEntity<Void> createEncounterChoice(
		@RequestBody @Validated EncounterChoiceSaveRequest request
	) {
		gameAdminUseCase.createEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/choices")
	public ResponseEntity<Void> deleteEncounterChoice(
		@RequestBody @Validated EncounterChoiceDeleteRequest request
	) {
		gameAdminUseCase.deleteEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}


