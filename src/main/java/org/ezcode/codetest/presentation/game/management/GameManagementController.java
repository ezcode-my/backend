package org.ezcode.codetest.presentation.game.management;

import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceSaveRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterSaveRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillSaveRequest;
import org.ezcode.codetest.application.game.management.GameAdminUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameManagementController {

	private final GameAdminUseCase gameAdminUseCase;

	@PostMapping("/items")
	public ResponseEntity<Void> createItem(
		@RequestBody @Valid ItemSaveRequest request
	) {
		gameAdminUseCase.createItem(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/items")
	public ResponseEntity<Void> deleteItem(
		@RequestBody @Valid ItemDeleteRequest request
	) {
		gameAdminUseCase.deleteItem(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/skills")
	public ResponseEntity<Void> createSkill(
		@RequestBody @Valid SkillSaveRequest request
	) {
		gameAdminUseCase.createSkill(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/skills")
	public ResponseEntity<Void> deleteSkill(
		@RequestBody @Valid SkillDeleteRequest request
	) {
		gameAdminUseCase.deleteSkill(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/encounters")
	public ResponseEntity<Void> createRandomEncounter(
		@RequestBody @Valid RandomEncounterSaveRequest request
	) {
		gameAdminUseCase.createRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/encounters")
	public ResponseEntity<Void> deleteRandomEncounter(
		@RequestBody @Valid RandomEncounterDeleteRequest request
	) {
		gameAdminUseCase.deleteRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/choices")
	public ResponseEntity<Void> createEncounterChoice(
		@RequestBody @Valid EncounterChoiceSaveRequest request
	) {
		gameAdminUseCase.createEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/choices")
	public ResponseEntity<Void> deleteEncounterChoice(
		@RequestBody @Valid EncounterChoiceDeleteRequest request
	) {
		gameAdminUseCase.deleteEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}


