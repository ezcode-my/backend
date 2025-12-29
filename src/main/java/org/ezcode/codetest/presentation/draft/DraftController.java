package org.ezcode.codetest.presentation.draft;

import java.util.Optional;

import org.ezcode.codetest.application.draft.dto.request.DraftSaveRequest;
import org.ezcode.codetest.application.draft.dto.response.DraftResponse;
import org.ezcode.codetest.application.draft.service.DraftService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drafts")
@RequiredArgsConstructor
@Tag(name = "Drafts", description = "코드 자동 저장 API")
public class DraftController {

	private final DraftService draftService;

	@Operation(
		summary = "코드 자동 저장",
		description = "작성 중인 코드를 자동으로 임시 저장합니다. 동일한 문제와 언어에 대한 기존 저장 내역이 있으면 업데이트하고, 없으면 새로 생성합니다. 버전 충돌 시 409 에러가 발생합니다."
	)
	@ApiResponse(responseCode = "200", description = "코드 저장 성공")
	@ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
	@ApiResponse(responseCode = "409", description = "다른 탭에서 저장된 최신 코드가 존재합니다. 새로고침이 필요합니다.")
	@PostMapping
	public ResponseEntity<DraftResponse> saveDraft(
		@Valid @RequestBody DraftSaveRequest request,
		@AuthenticationPrincipal AuthUser authUser
	) {

		DraftResponse response = draftService.autoSave(authUser.getId(), request);

		return ResponseEntity.ok(response);
	}

	@Operation(
		summary = "저장된 코드 조회",
		description = "특정 문제와 언어에 대해 저장된 임시 저장 코드를 조회합니다. 저장된 데이터가 없으면 result가 null로 반환됩니다.",
		parameters = {
			@Parameter(name = "problemId", description = "문제 ID", required = true),
			@Parameter(name = "languageId", description = "언어 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "저장된 코드 조회 성공 (데이터가 없을 경우 result는 null)")
	@GetMapping
	public ResponseEntity<DraftResponse> getDraft(
		@RequestParam Long problemId,
		@RequestParam Long languageId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		Optional<DraftResponse> response = draftService.getDraft(
			authUser.getId(),
			problemId,
			languageId
		);

		return response
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.ok().body(null));
	}
}
