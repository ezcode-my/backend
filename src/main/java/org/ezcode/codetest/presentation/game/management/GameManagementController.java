package org.ezcode.codetest.presentation.game.management;

import java.util.List;

import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.EncounterChoiceSaveRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.item.ItemSaveRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.encounter.RandomEncounterSaveRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillDeleteRequest;
import org.ezcode.codetest.application.game.dto.request.skill.SkillSaveRequest;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterChoiceResponse;
import org.ezcode.codetest.application.game.dto.response.encounter.EncounterResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.application.game.management.GameAdminUseCase;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class GameManagementController {

	private final GameAdminUseCase gameAdminUseCase;

	@Operation(
		summary = "아이템 생성 API(어드민 전용)",
		description = "아이템을 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "생성 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 아이템이 생성되었습니다.")
	@PostMapping("/items")
	public ResponseEntity<Void> createItem(
		@RequestBody @Valid ItemSaveRequest request
	) {
		gameAdminUseCase.createItem(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "아이템 삭제 API(어드민 전용)",
		description = "아이템을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "삭제 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 아이템이 삭제되었습니다.")
	@DeleteMapping("/items")
	public ResponseEntity<Void> deleteItem(
		@RequestBody @Valid ItemDeleteRequest request
	) {
		gameAdminUseCase.deleteItem(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "아이템 조회 API(어드민 전용)",
		description = "아이템을 전부 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 아이템이 조회되었습니다.")
	@GetMapping("/items")
	public ResponseEntity<List<ItemResponse>> getAllItems(
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gameAdminUseCase.getAllItems());
	}

	@Operation(
		summary = "스킬 생성 API(어드민 전용)",
		description = "스킬을 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "생성 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬이 생성되었습니다.")
	@PostMapping("/skills")
	public ResponseEntity<Void> createSkill(
		@RequestBody @Valid SkillSaveRequest request
	) {
		gameAdminUseCase.createSkill(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "스킬 삭제 API(어드민 전용)",
		description = "스킬을 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "삭제 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬이 삭제되었습니다.")
	@DeleteMapping("/skills")
	public ResponseEntity<Void> deleteSkill(
		@RequestBody @Valid SkillDeleteRequest request
	) {
		gameAdminUseCase.deleteSkill(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "스킬 조회 API(어드민 전용)",
		description = "스킬을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 스킬이 조회되었습니다.")
	@GetMapping("/skills")
	public ResponseEntity<List<SkillResponse>> getAllSkills(
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gameAdminUseCase.getAllSkills());
	}

	@Operation(
		summary = "랜덤 인카운터 생성 API(어드민 전용)",
		description = "랜덤 인카운터를 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "생성 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터가 생성되었습니다.")
	@PostMapping("/encounters")
	public ResponseEntity<Void> createRandomEncounter(
		@RequestBody @Valid RandomEncounterSaveRequest request
	) {
		gameAdminUseCase.createRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "랜덤 인카운터 삭제 API(어드민 전용)",
		description = "랜덤 인카운터를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "삭제 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터가 삭제되었습니다.")
	@DeleteMapping("/encounters")
	public ResponseEntity<Void> deleteRandomEncounter(
		@RequestBody @Valid RandomEncounterDeleteRequest request
	) {
		gameAdminUseCase.deleteRandomEncounter(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "랜덤 인카운터 조회 API(어드민 전용)",
		description = "랜덤 인카운터를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터가 조회되었습니다.")
	@GetMapping("/encounters")
	public ResponseEntity<List<EncounterResponse>> getAllRandomEncounters(
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gameAdminUseCase.getAllRandomEncounters());
	}

	@Operation(
		summary = "랜덤 인카운터 선택지 생성 API(어드민 전용)",
		description = "랜덤 인카운터 선택지를 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "생성 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터 선택지가 생성되었습니다.")
	@PostMapping("/choices")
	public ResponseEntity<Void> createEncounterChoice(
		@RequestBody @Valid EncounterChoiceSaveRequest request
	) {
		gameAdminUseCase.createEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(
		summary = "랜덤 인카운터 선택지 삭제 API(어드민 전용)",
		description = "랜덤 인카운터 선택지를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "삭제 후, 여부 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터 선택지가 삭제되었습니다.")
	@DeleteMapping("/choices")
	public ResponseEntity<Void> deleteEncounterChoice(
		@RequestBody @Valid EncounterChoiceDeleteRequest request
	) {
		gameAdminUseCase.deleteEncounterChoice(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "랜덤 인카운터 선택지 조회 API(어드민 전용)",
		description = "랜덤 인카운터 선택지를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 결과 반환")
		}
	)
	@ResponseMessage("정상적으로 인카운터 선택지가 조회되었습니다.")
	@GetMapping("/choices")
	public ResponseEntity<List<EncounterChoiceResponse>> getAllEncounterChoices(
	) {
		return ResponseEntity.status(HttpStatus.OK).body(gameAdminUseCase.getAllEncounterChoices());
	}

}


