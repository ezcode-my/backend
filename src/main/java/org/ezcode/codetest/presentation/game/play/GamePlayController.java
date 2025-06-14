package org.ezcode.codetest.presentation.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.request.ItemEquipRequest;
import org.ezcode.codetest.application.game.dto.request.ItemGamblingRequest;
import org.ezcode.codetest.application.game.dto.request.SkillEquipRequest;
import org.ezcode.codetest.application.game.dto.response.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.SkillGamblingResponse;
import org.ezcode.codetest.application.game.play.GamePlayUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GamePlayController {

	private final GamePlayUseCase gamePlayUseCase;

	@PostMapping("/characters")
	public ResponseEntity<Void> createCharacter(
		@AuthenticationPrincipal AuthUser authUser
	) {
		gamePlayUseCase.createCharacter(authUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/characters")
	public ResponseEntity<CharacterStatusResponse> CharacterStatusOpen(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gamePlayUseCase.characterStatusOpen(authUser.getId()));
	}

	@PostMapping("/items/gambling")
	public ResponseEntity<ItemGamblingResponse> gamblingForItem(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Validated ItemGamblingRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(gamePlayUseCase.gamblingForItem(authUser.getId(), request.itemCategory()));
	}

	@PostMapping("/skills/gambling")
	public ResponseEntity<SkillGamblingResponse> gamblingForSkill(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(gamePlayUseCase.gamblingForSkill(authUser.getId()));
	}

	@GetMapping("/inventories")
	public ResponseEntity<List<ItemResponse>> inventoryOpen(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.inventoryOpen(authUser.getId()));
	}

	@PatchMapping("/items/equip")
	public ResponseEntity<Void> equipItem(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Validated ItemEquipRequest request
	) {
		gamePlayUseCase.equipItem(authUser.getId(), request.name());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/skills/equip")
	public ResponseEntity<Void> equipSkill(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Validated SkillEquipRequest request
	) {
		gamePlayUseCase.equipSkill(authUser.getId(), request.name());

		return ResponseEntity.status(HttpStatus.OK).build();
	}


}
