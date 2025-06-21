package org.ezcode.codetest.presentation.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.request.encounter.BattleRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemEquipRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemGamblingRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillEquipRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillUnEquipRequest;
import org.ezcode.codetest.application.game.dto.response.character.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.BattleHistoryResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterResultResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingBattleResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.MatchingEncounterResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.application.game.play.GamePlayUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GamePlayController {

	private final GamePlayUseCase gamePlayUseCase;

	@Operation(
		summary = "게임 캐릭터 생성 API",
		description = "현재 사용자의 캐릭터를 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "생성 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 캐릭터 생성에 성공하였습니다.")
	@PostMapping("/characters")
	public ResponseEntity<Void> createCharacter(
		@AuthenticationPrincipal AuthUser authUser
	) {
		gamePlayUseCase.createCharacter(authUser.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "스테이터스 조회 API",
		description = "현재 캐릭터의 상태를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "사용자 상태 반환")
		}
	)
	@ResponseMessage("정상적으로 캐릭터 스텟이 조회되었습니다.")
	@GetMapping("/characters")
	public ResponseEntity<CharacterStatusResponse> CharacterStatusOpen(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gamePlayUseCase.characterStatusOpen(authUser.getId()));
	}

	@Operation(
		summary = "캐릭터 보유 스킬 조회 API",
		description = "현재 캐릭터의 장착하지 않은 보유 스킬들을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "사용자 스킬 보유 반환")
		}
	)
	@ResponseMessage("정상적으로 캐릭터 보유 스킬이 조회되었습니다.")
	@GetMapping("/characters/skills/unequipped")
	public ResponseEntity<List<SkillResponse>> CharacterSkillsOpen(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gamePlayUseCase.skillInventoryOpen(authUser.getId()));
	}

	@Operation(
		summary = "아이템 뽑기 API",
		description = "현재 캐릭터의 골드를 소모해 아이템을 뽑습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "뽑기 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 아이템 뽑기에 성공하였습니다.")
	@PostMapping("/characters/items/gamble")
	public ResponseEntity<ItemGamblingResponse> gamblingForItem(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid ItemGamblingRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(gamePlayUseCase.gamblingForItem(authUser.getId(), request.itemCategory()));
	}

	@Operation(
		summary = "스킬 뽑기 API",
		description = "현재 캐릭터의 골드를 소모해 스킬을 뽑습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "뽑기 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬 뽑기에 성공하였습니다.")
	@PostMapping("/characters/skills/gamble")
	public ResponseEntity<SkillGamblingResponse> gamblingForSkill(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(gamePlayUseCase.gamblingForSkill(authUser.getId()));
	}

	@Operation(
		summary = "인벤토리 조회 API",
		description = "현재 캐릭터의 인벤토리를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 인벤토리가 조회되었습니다.")
	@GetMapping("/characters/inventories")
	public ResponseEntity<List<ItemResponse>> inventoryOpen(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.inventoryOpen(authUser.getId()));
	}

	@Operation(
		summary = "아이템 장착 API",
		description = "현재 캐릭터의 아이템을 장착합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "장착 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 아이템이 장착되었습니다.")
	@PatchMapping("/characters/items/equip")
	public ResponseEntity<Void> equipItem(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid ItemEquipRequest request
	) {
		gamePlayUseCase.equipItem(authUser.getId(), request.name());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "스킬 장착 API",
		description = "현재 캐릭터의 스킬을 장착합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "장착 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬이 장착되었습니다.")
	@PatchMapping("/characters/skills/equip")
	public ResponseEntity<Void> equipSkill(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid SkillEquipRequest request
	) {
		gamePlayUseCase.equipSkill(authUser.getId(), request);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "스킬 장착 해제 API",
		description = "현재 캐릭터의 스킬을 장착 해제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "장착 해제 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬 장착이 해제되었습니다.")
	@PatchMapping("/characters/skills/unequip")
	public ResponseEntity<Void> unEquipSkill(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid SkillUnEquipRequest request
	) {
		gamePlayUseCase.unEquipSkill(authUser.getId(), request);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "배틀 API",
		description = "지정된 다른 캐릭터와 배틀을 진행합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "배틀 진행 후, 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 배틀이 완료되었습니다.")
	@PostMapping("/characters/battles")
	public ResponseEntity<BattleHistoryResponse> battle(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid BattleRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.battle(authUser.getId(), request));
	}

	@Operation(
		summary = "무작위 배틀 매칭 API",
		description = "무작위로 다른 캐릭터 중 하나를 매칭시켜줍니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "무작위 캐릭터 반환")
		}
	)
	@ResponseMessage("정상적으로 배틀 매칭에 성공하였습니다.")
	@GetMapping("/characters/battles/matching")
	public ResponseEntity<MatchingBattleResponse> randomBattleMatching(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.randomBattleMatching(authUser.getId()));
	}

	@Operation(
		summary = "랜덤 인카운터 매칭 API",
		description = "무작위로 랜덤 인카운터 중 하나를 매칭시켜줍니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "무작위 인카운터 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터 매칭에 성공하였습니다.")
	@GetMapping("/characters/encounters/matching")
	public ResponseEntity<MatchingEncounterResponse> randomEncounterMatching(
		@AuthenticationPrincipal AuthUser authUser
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.randomEncounterMatching(authUser.getId()));
	}

	@Operation(
		summary = "랜덤 인카운터 선택지 결정 API",
		description = "게임 캐릭터의 다음 행동을 결정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "인카운터 선택지에 대한 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터 선택지가 결정되었습니다.")
	@PostMapping("/characters/encounters/choice")
	public ResponseEntity<EncounterResultResponse> encounterChoice(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody @Valid EncounterChoiceRequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(gamePlayUseCase.encounterChoice(authUser.getId(), request));
	}

}
