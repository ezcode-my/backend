package org.ezcode.codetest.presentation.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemEquipRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemGamblingRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillEquipRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillUnEquipRequest;
import org.ezcode.codetest.application.game.dto.response.character.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.BattleHistoryResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingBattleResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingEncounterResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillGamblingResponse;
import org.ezcode.codetest.application.game.play.GamePlayUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
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
		@RequestBody @Valid ItemGamblingRequest request
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
		@RequestBody @Valid ItemEquipRequest request
	) {
		gamePlayUseCase.equipItem(authUser.getId(), request.name());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/skills/equip")
	public ResponseEntity<Void> equipSkill(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid SkillEquipRequest request
	) {
		gamePlayUseCase.equipSkill(authUser.getId(), request);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/skills/unequip")
	public ResponseEntity<Void> unEquipSkill(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid SkillUnEquipRequest request
	) {
		gamePlayUseCase.unEquipSkill(authUser.getId(), request);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/battles/{enemyId}")
	public ResponseEntity<BattleHistoryResponse> battle(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long enemyId
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.battle(authUser.getId(), enemyId));
	}

	@PostMapping("/battles")
	public ResponseEntity<BattleHistoryResponse> randomBattle(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.randomBattle(authUser.getId()));
	}

	@GetMapping("/battles/matching")
	public ResponseEntity<MatchingBattleResponse> randomBattleMatching(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.randomBattleMatching(authUser.getId()));
	}

	@GetMapping("/encounters/matching")
	public ResponseEntity<MatchingEncounterResponse> randomEncounterMatching(
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.randomEncounterMatching());
	}

	@PostMapping("/encounters/{encounterId}")
	public ResponseEntity<EncounterResponse> encounterChoice(
		@AuthenticationPrincipal AuthUser authUser,
		@PathVariable Long encounterId,
		@RequestBody @Valid EncounterChoiceRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.encounterChoice(authUser.getId(), encounterId, request));
	}

}
